package com.LGS.service;

import com.LGS.dao.SensorData.SensorDataDao;
import com.LGS.model.Sensory.SensorDataBundle;
import com.LGS.model.View.Sensory.SensorDataBundleView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class SensorDataServiceImpl implements SensorDataService {

    private final SensorDataDao sensorDataDao;

    @Autowired
    public SensorDataServiceImpl(@Qualifier("postgres_SensorData") SensorDataDao sensorDataDao) {
        this.sensorDataDao = sensorDataDao;
    }

    @Override
    public int insertDataUnprocessed(SensorDataBundle sensorData) {
        return sensorDataDao.insertDataUnprocessed(sensorData);
    }

    @Override
    public int insertDataProcessed(SensorDataBundle sensorData, int riskLevel, String threatLevel) {
        return sensorDataDao.insertDataProcessed(sensorData, riskLevel, threatLevel);
    }

    @Override
    public Optional<SensorDataBundleView> selectSensorDataByUserId(UUID userId) {
        return sensorDataDao.selectSensorDataByUserId(userId);
    }


    @Override
    public int deleteSensorDataBundle(SensorDataBundle sensorDataBundle) {
        return sensorDataDao.deleteSensorDataBundle(sensorDataBundle);
    }

}
