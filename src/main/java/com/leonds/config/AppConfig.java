package com.leonds.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Leon
 */
@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppConfig {
    private Jwt jwt;
    private String fileLocation;

    @Data
    public static class Jwt {
        private String header;
        private String secret;
        private long expiration;
    }

}
