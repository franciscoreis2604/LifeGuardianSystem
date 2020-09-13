package com.LGS.model.SensorHandlers;

public class Pulse extends SensorHandler {

    public Pulse() {
    }

    @Override
    public int calculateCautionLevelForSensor(Float pulseValue) {
        if (isBetween(pulseValue, 50f, 59f) || isBetween(pulseValue, 101f, 119f)) {
            return 1;
        } else if (isBetween(pulseValue, 40f, 49f) || isBetween(pulseValue, 120f, 149f)) {
            return 2;
        } else if (pulseValue < 40 || pulseValue >= 150) {
            return 5;
        } else {
            return 0;
        }
    }
}
