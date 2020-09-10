package com.LGS.model.Risk;

import com.LGS.exception.ApiRequestException;
import com.LGS.model.SensorHandlers.RiskHandler;
import com.LGS.model.Sensory.SensorDataBundle;
import com.LGS.model.Sensory.SensorPacket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstantRiskCalculator {
    private SensorDataBundle sensorDataBundle;

    private int riskLevel;
    private String riskDescription;
    private int numberOfSensors;

    public InstantRiskCalculator(SensorDataBundle sensorDataBundle) {
        this.sensorDataBundle = sensorDataBundle;
        this.numberOfSensors = sensorDataBundle.getSensorData().size();
    }

    /**
     * This method cycles over all packets in each bundle and finds a suiting handler to process a given
     * sensor value and provide a risk value of 1,2 or 5 according to the given value.
     *
     */
    public void calculateRisk() {
        riskLevel = 0;
        for (SensorPacket packet : sensorDataBundle.getSensorData()){
            try {
                Class<?> handler = Class.forName("com.LGS.model.SensorHandlers." + packet.getSensorType());
                riskLevel += ((RiskHandler) handler.getConstructor().newInstance()).calculateRisk(packet.getSensorReading());
            }catch (Exception e){
                throw new ApiRequestException("Unsupported sensor");
            }
        }
        this.riskDescription = calculateRiskDescription(riskLevel);
    }

    private String calculateRiskDescription(int riskLevel) {
        int maxValue = 5 * numberOfSensors;
        if (riskLevel == 0) return "Safe";
        if (isBetween(riskLevel, 0f, 0.30f * maxValue)) return Risk.LOW_RISK.getRiskDescription();
        if (isBetween(riskLevel, 0.30f * maxValue, 0.60f * maxValue)) return Risk.MEDIUM_RISK.getRiskDescription();
        if (isBetween(riskLevel, 0.60f * maxValue, maxValue)) return Risk.HIGH_RISK.getRiskDescription();

        return "";
    }

    private boolean isBetween(float x, float lower, float upper) {
        return lower <= x && x <= upper;
    }

    public String toString() {
        return String.format("\n Risk Level : %d \n Risk Description: %s", this.riskLevel, this.riskDescription);
    }

    public void setSensorDataBundle(SensorDataBundle sensorDataBundle) {
        this.sensorDataBundle = sensorDataBundle;
        this.numberOfSensors = sensorDataBundle.getSensorData().size();
    }
}
