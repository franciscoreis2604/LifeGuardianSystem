package com.LGS.model.SensorHandlers;

public abstract class RiskHandler {

    public abstract int calculateRisk(Float sensorValue);

    public boolean isBetween(float x, float lower, float upper) {
        return lower <= x && x <= upper;
    }

}
