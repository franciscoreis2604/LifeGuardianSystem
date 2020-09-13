package com.LGS.model.Caution;

import com.LGS.model.Sensory.SensorPacket;
import com.LGS.model.View.Notifications.RiskNotification;
import com.LGS.model.View.Sensory.SensorDataBundleView;
import com.LGS.service.NotificationServiceImplRbtMQ;

import java.util.UUID;


public class DelayedCautionCalculator {
    private final NotificationServiceImplRbtMQ notificationServiceImplRbtMQ;
    private UUID userId;
    private Caution cautionType;
    private SensorDataBundleView bundleToProcess;
    private int totalNumberOfPackets;

    public DelayedCautionCalculator(NotificationServiceImplRbtMQ notificationServiceImplRbtMQ, UUID userId, Caution cautionType, SensorDataBundleView sensorDataBundleView) {
        this.notificationServiceImplRbtMQ = notificationServiceImplRbtMQ;
        this.userId = userId;
        this.bundleToProcess = sensorDataBundleView;
        this.cautionType = cautionType;
        this.totalNumberOfPackets = sensorDataBundleView.getSensorData().size();

    }

    public void calculateDelayedCautionLevel() {
        int packetsAtHigh = 0;
        for (SensorPacket sensorPacket : bundleToProcess.getSensorData()) {
            String cautionDescription = sensorPacket.getCautionDescription();
            if (cautionDescription.equals(cautionType.getCautionDescription()))
                packetsAtHigh++;
        }

        if ((packetsAtHigh > totalNumberOfPackets / 2) && (totalNumberOfPackets > cautionType.getMinimumNumberOfPackets())) {
            notificationServiceImplRbtMQ.send(userId.toString(), new RiskNotification(cautionType.getCautionDescription()).toString());

        }
    }
}
