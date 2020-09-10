package com.LGS.model.Sensory;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class SensorDataBundle {

    private final UUID id;
    private final UUID userId;
    private final List<SensorPacket> sensorData;

    public SensorDataBundle(UUID id,
                            @JsonProperty("userId")UUID userId,
                            @JsonProperty("sensorData") List<SensorPacket> sensorData) {
        this.id = id;
        this.userId = userId;
        this.sensorData = sensorData;
    }
}
