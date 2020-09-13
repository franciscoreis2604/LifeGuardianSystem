package com.LGS.model.View.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
public class UserView {


    @NotBlank
    @Setter
    private String name;

    @NotBlank
    @Setter
    private String phoneNumber;

    @Setter
    private Date birthDate;

    public UserView(@JsonProperty("name") String name,
                    @JsonProperty("phoneNumber") String phoneNumber,
                    @JsonProperty("birthDate") Date birthDate) {

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
    }
}

