package com.LGS.model.SensorHandlers;

public abstract class SensorHandler {

    public abstract int calculateCautionLevelForSensor(Float sensorValue);

    public boolean isBetween(float x, float lower, float upper) {
        return lower <= x && x <= upper;
    }

}