package com.LGS.model.SensorHandlers;

public class Temperature extends RiskHandler {

    public Temperature() {
    }

    @Override
    public int calculateRisk(Float temperatureValue) {
        if (isBetween(temperatureValue, 35f, 35.9f) || isBetween(temperatureValue, 38f, 38.9f)) {
            return 1;
        } else if (isBetween(temperatureValue, 34f, 34.9f) || isBetween(temperatureValue, 39f, 39.9f)) {
            return 2;
        } else if (temperatureValue < 34 || temperatureValue >= 40) {
            return 5;
        } else {
            return 0;
        }
    }
}
