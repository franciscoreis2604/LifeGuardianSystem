package com.LGS.model.Caution;

import com.LGS.model.Sensory.SensorDataBundle;
import lombok.Getter;
import org.json.JSONObject;

public class InstantMonitoringEvent {

    @Getter
    private InstantCautionCalculator instantCautionCalculator;

    public InstantMonitoringEvent(SensorDataBundle sensorData) {
        this.instantCautionCalculator = new InstantCautionCalculator(sensorData);
    }

    public JSONObject monitor() {
        instantCautionCalculator.calculateInstantCautionLevel();
        JSONObject resultAsJsonStr = new JSONObject();
        resultAsJsonStr.put("Caution level", instantCautionCalculator.getCautionLevel());
        resultAsJsonStr.put("Caution level description", instantCautionCalculator.getCautionDescription());
        return resultAsJsonStr;

    }

}
