package com.LGS.model.Risk;

import com.LGS.model.Sensory.SensorDataBundle;
import lombok.Getter;
import org.json.JSONObject;

public class InstantMonitoringEvent {

    @Getter
    private InstantRiskCalculator instantRiskCalculator;

    public InstantMonitoringEvent(SensorDataBundle sensorData) {
        this.instantRiskCalculator = new InstantRiskCalculator(sensorData);
    }

    public JSONObject monitor() {
        instantRiskCalculator.calculateRisk();
        JSONObject resultAsJsonStr = new JSONObject();
        resultAsJsonStr.put("Risk Level", instantRiskCalculator.getRiskLevel());
        resultAsJsonStr.put("Risk Description", instantRiskCalculator.getRiskDescription());
        return resultAsJsonStr;

    }

}
