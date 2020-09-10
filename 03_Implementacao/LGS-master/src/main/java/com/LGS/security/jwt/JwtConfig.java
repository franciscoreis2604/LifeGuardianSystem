package com.LGS.security.jwt;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private Integer tokenExpirationAfterDays;
    private String authorizationHeader;

    public JwtConfig() {
        authorizationHeader = HttpHeaders.AUTHORIZATION;
    }

    @Bean
    public SecretKey secretKeyBytes(){
        return Keys.hmacShaKeyFor(getSecretKey().getBytes());
    }
}
