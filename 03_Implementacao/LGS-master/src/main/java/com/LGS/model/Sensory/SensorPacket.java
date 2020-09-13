package com.LGS.model.Sensory;

import com.LGS.exception.ApiRequestException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
public class SensorPacket {
    String sensorType;
    float sensorReading;
    Timestamp timestamp;
    private int cautionLevel;
    private String cautionDescription;

    public SensorPacket() {
    }

    /**
     * Builds a Sensor Packet from a JSON Object
     *
     * @param sensorType    - "Temperature,Pulse,movement"
     * @param sensorReading -"String in order to resolve empty field issue"
     * @param timeStamp     - "Time at which reading was given"
     */
    public SensorPacket(@JsonProperty("name") String sensorType, @NotNull @JsonProperty("value") String sensorReading, Timestamp timeStamp) {
        this.sensorType = sensorType.substring(0, 1).toUpperCase() + sensorType.substring(1).toLowerCase();
        try {
            this.sensorReading = Float.parseFloat(sensorReading);
        } catch (Exception e) {
            throw new ApiRequestException("Field cannot be empty");
        }
        this.timestamp = timeStamp;
    }

    public SensorPacket(String sensorType, String sensorReading, Timestamp timestamp, int cautionLevel, String cautionDescription) {
        this.sensorType = sensorType;
        try {
            this.sensorReading = Float.parseFloat(sensorReading);
        } catch (Exception e) {
            throw new ApiRequestException("Field cannot be empty");
        }
        this.timestamp = timestamp;
        this.cautionLevel = cautionLevel;
        this.cautionDescription = cautionDescription;
    }
}
