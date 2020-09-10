package com.LGS.service;

import com.LGS.dao.SensorData.SensorDataDao;
import com.LGS.dao.User.UserDataAccessService;
import com.LGS.exception.ApiRequestException;
import com.LGS.model.Risk.DelayedRiskCalculator;
import com.LGS.model.Risk.InstantMonitoringEvent;
import com.LGS.model.Risk.Risk;
import com.LGS.model.Sensory.SensorDataBundle;
import com.LGS.model.View.Sensory.SensorDataBundleView;
import com.LGS.model.View.User.UserView;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MonitorServiceImpl implements MonitorService {

    private final SensorDataDao sensorDataDao;
    private final UserDataAccessService userDataAccessService;
    private final NotificationServiceImplRbtMQ notificationServiceImplRbtMQ;


    private List<UUID> usersThatRequestedMonitoring = new ArrayList<>();

    @Autowired
    public MonitorServiceImpl(@Qualifier("postgres_SensorData") SensorDataDao sensorDataDao, UserDataAccessService userDataAccessService,
                              NotificationServiceImplRbtMQ notificationServiceImplRbtMQ) {
        this.sensorDataDao = sensorDataDao;
        this.userDataAccessService = userDataAccessService;
        this.notificationServiceImplRbtMQ = notificationServiceImplRbtMQ;
    }

    public JSONObject instantMonitor(SensorDataBundle sensorData) {
        Optional<UserView> userView = userDataAccessService.verifyUserUUID(sensorData.getUserId());
        if (userView.isEmpty()) throw new ApiRequestException("User doesn't exist");

        InstantMonitoringEvent event = new InstantMonitoringEvent(sensorData);
        if (!usersThatRequestedMonitoring.contains(sensorData.getUserId()))
            usersThatRequestedMonitoring.add(sensorData.getUserId());
        return event.monitor();
    }

    @Scheduled(fixedDelayString = "PT30M")
    public void delayedHighRiskMonitor() {
        String riskMeasurement = "sprocesseddelayedhigh";
        for (UUID userID : usersThatRequestedMonitoring) {
            SensorDataBundleView toBeProcessedHigh = sensorDataDao.selectUnprocessedSensorDataByUserId(userID, riskMeasurement).
                    orElseThrow(() -> new ApiRequestException("Nothing new to be processed"));
            DelayedRiskCalculator delayedRiskCalculator = new DelayedRiskCalculator(notificationServiceImplRbtMQ, userID, Risk.HIGH_RISK, toBeProcessedHigh);
            delayedRiskCalculator.calculateDelayedRisk();
            sensorDataDao.updateSensorDataByUserId(userID, toBeProcessedHigh, riskMeasurement);
        }
    }

    @Scheduled(fixedDelayString = "PT6H")
    public void delayedMediumRiskMonitor() {
        String riskMeasurement = "sprocesseddelayedmedium";
        for (UUID userID : usersThatRequestedMonitoring) {
            SensorDataBundleView toBeProcessedMedium = sensorDataDao.selectUnprocessedSensorDataByUserId(userID, riskMeasurement).
                    orElseThrow(() -> new ApiRequestException("Nothing new to be processed"));
            DelayedRiskCalculator delayedRiskCalculator = new DelayedRiskCalculator(notificationServiceImplRbtMQ, userID, Risk.MEDIUM_RISK, toBeProcessedMedium);
            delayedRiskCalculator.calculateDelayedRisk();
            sensorDataDao.updateSensorDataByUserId(userID, toBeProcessedMedium, riskMeasurement);
        }

    }

    @Scheduled(fixedDelayString = "PT24H")
    public void delayedLowRiskMonitor() {
        String riskMeasurement = "sprocesseddelayedlow";
        for (UUID userID : usersThatRequestedMonitoring) {
            SensorDataBundleView toBeProcessedLow = sensorDataDao.selectUnprocessedSensorDataByUserId(userID, riskMeasurement).
                    orElseThrow(() -> new ApiRequestException("Nothing new to be processed"));
            DelayedRiskCalculator delayedRiskCalculator = new DelayedRiskCalculator(notificationServiceImplRbtMQ, userID, Risk.LOW_RISK, toBeProcessedLow);
            delayedRiskCalculator.calculateDelayedRisk();
            sensorDataDao.updateSensorDataByUserId(userID, toBeProcessedLow, riskMeasurement);
        }
    }
}
