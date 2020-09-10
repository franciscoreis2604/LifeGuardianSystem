package com.LGS.model.SOSContact;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SOSContactPacket {

    @NotBlank
    private String contactType;

    @NotBlank
    private String contactValue;

    /**
     * Corresponds to one of many possible types of contact to one SOS contact
     *
     * @param contactType  "Email,Mobile,Telephone"
     * @param contactValue " dependant on the type but generic"
     */
    public SOSContactPacket(@JsonProperty("name")  String contactType, @JsonProperty("value") String contactValue) {
        this.contactType = contactType.substring(0, 1).toUpperCase() + contactType.substring(1).toLowerCase();
        this.contactValue = contactValue;
    }
}
