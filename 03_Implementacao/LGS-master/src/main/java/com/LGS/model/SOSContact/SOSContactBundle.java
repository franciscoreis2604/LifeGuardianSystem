package com.LGS.model.SOSContact;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SOSContactBundle {

    @Setter(AccessLevel.NONE)
    private final UUID uId;

    @NotBlank
    private String name;

    @NotNull
    private List<SOSContactPacket> contactInfo;

    /**
     * SOSContact per-se
     * Corresponds to a SOS contact that contains many possible types of contact
     * @param uId
     * @param name
     * @param contactInfo
     */

    public SOSContactBundle(@JsonProperty("userId") UUID uId, @JsonProperty("contactName") String name,
                            @JsonProperty("contactInfo") List<SOSContactPacket> contactInfo) {
        this.uId = uId;
        this.name = name;
        this.contactInfo = contactInfo;
    }
}
