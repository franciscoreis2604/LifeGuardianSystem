package com.LGS.model.Risk;

import com.LGS.model.Sensory.SensorPacket;
import com.LGS.model.View.Notifications.RiskNotification;
import com.LGS.model.View.Sensory.SensorDataBundleView;
import com.LGS.service.NotificationServiceImplRbtMQ;

import java.util.UUID;


public class DelayedRiskCalculator {
    private final NotificationServiceImplRbtMQ notificationServiceImplRbtMQ;
    private UUID userId;
    private Risk riskType;
    private SensorDataBundleView bundleToProcess;
    private int totalNumberOfPackets;

    public DelayedRiskCalculator(NotificationServiceImplRbtMQ notificationServiceImplRbtMQ, UUID userId, Risk riskType, SensorDataBundleView sensorDataBundleView) {
        this.notificationServiceImplRbtMQ = notificationServiceImplRbtMQ;
        this.userId = userId;
        this.bundleToProcess = sensorDataBundleView;
        this.riskType = riskType;
        this.totalNumberOfPackets = sensorDataBundleView.getSensorData().size();

    }

    public void calculateDelayedRisk() {
        int packetsAtHigh = 0;
        for (SensorPacket sensorPacket : bundleToProcess.getSensorData()) {
            String riskDescription = sensorPacket.getRiskDescription();
            if (riskDescription.equals(riskType.getRiskDescription()))
                packetsAtHigh++;
        }
        if ((packetsAtHigh > totalNumberOfPackets / 2) && (totalNumberOfPackets > riskType.getMinimumNumberOfPackets())) {
            notificationServiceImplRbtMQ.send(userId.toString(), new RiskNotification(riskType.getRiskDescription()).toString());

        }
    }
}
