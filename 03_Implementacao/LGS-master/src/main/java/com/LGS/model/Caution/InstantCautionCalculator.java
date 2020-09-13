package com.LGS.model.Caution;

import com.LGS.exception.ApiRequestException;
import com.LGS.model.SensorHandlers.SensorHandler;
import com.LGS.model.Sensory.SensorDataBundle;
import com.LGS.model.Sensory.SensorPacket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstantCautionCalculator {
    private SensorDataBundle sensorDataBundle;

    private int cautionLevel;
    private String cautionDescription;
    private int numberOfSensors;

    public InstantCautionCalculator(SensorDataBundle sensorDataBundle) {
        this.sensorDataBundle = sensorDataBundle;
        this.numberOfSensors = sensorDataBundle.getSensorData().size();
    }

    /**
     * This method cycles over all packets in each bundle and finds a suiting handler to process a given
     * sensor value and provide a caution level value of 1,2 or 5 according to the given value.
     */
    public void calculateInstantCautionLevel() {
        cautionLevel = 0;
        for (SensorPacket packet : sensorDataBundle.getSensorData()) {
            try {
                Class<?> handler = Class.forName("com.LGS.model.SensorHandlers." + packet.getSensorType());
                cautionLevel += ((SensorHandler) handler.getConstructor().newInstance()).calculateCautionLevelForSensor(packet.getSensorReading());
            } catch (Exception e) {
                throw new ApiRequestException("Unsupported sensor");
            }
        }
        this.cautionDescription = calculateCautionLevelDescription(cautionLevel);
    }

    private String calculateCautionLevelDescription(int riskLevel) {
        int maxValue = 5 * numberOfSensors;
        if (riskLevel == 0) return "Safe";
        if (isBetween(riskLevel, 0f, 0.30f * maxValue)) return Caution.LOW_CAUTION.getCautionDescription();
        if (isBetween(riskLevel, 0.30f * maxValue, 0.60f * maxValue))
            return Caution.MEDIUM_CAUTION.getCautionDescription();
        if (isBetween(riskLevel, 0.60f * maxValue, maxValue)) return Caution.HIGH_CAUTION.getCautionDescription();

        return "";
    }

    private boolean isBetween(float x, float lower, float upper) {
        return lower <= x && x <= upper;
    }

    public String toString() {
        return String.format("\n Caution Level : %d \n Caution Description: %s", this.cautionLevel, this.cautionDescription);
    }

    public void setSensorDataBundle(SensorDataBundle sensorDataBundle) {
        this.sensorDataBundle = sensorDataBundle;
        this.numberOfSensors = sensorDataBundle.getSensorData().size();
    }
}
