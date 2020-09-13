package com.LGS.model.View.Sensory;

import com.LGS.model.Sensory.SensorPacket;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SensorDataBundleView {

    private final List<SensorPacket> sensorData;

    public SensorDataBundleView(List<SensorPacket> sensorData) {
        this.sensorData = sensorData;
    }
}
