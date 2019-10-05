package com.hton.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "server")
@Data
public class ServerConfigurator {
    private Long port;
    private String keyPassword;
    private String keyStore;
    private String keyStoreProvider;
    private String keyStoreType;
}
