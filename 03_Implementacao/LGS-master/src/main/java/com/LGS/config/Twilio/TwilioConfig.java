package com.LGS.config.Twilio;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.twilio")
@Getter
@Setter
public class TwilioConfig {

    private String accountSid;
    private String authToken;
    private String trailNumber;

    public TwilioConfig() {
    }
}
