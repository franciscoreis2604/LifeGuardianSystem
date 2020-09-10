package com.LGS.model.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
public class UserCredentials  {

    private final UUID id;

    @NotBlank
    @Setter
    private String email;

    @NotBlank
    @Setter
    private String password;


    public UserCredentials(UUID id,
                     @JsonProperty("email") String email,
                     @JsonProperty("password") String password) {

        this.id = id;
        this.email = email;
        this.password = password;
    }
}
