package com.hton.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TokenCache {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private Map<String, String> tokenCache = new HashMap<>();

    @Scheduled(fixedRate = 60_000)
    public void clearCache() {
        log.info("Clearing cache");
        tokenCache.entrySet().removeIf(entry -> jwtTokenUtil.isTokenExpired(entry.getValue()));
        log.info("Cache cleared");
    }

    public String getToken(String login) {
        return tokenCache.get(login);
    }

    public void addToken(String login, String token) {
        log.info("Adding token for login {}", login);
        tokenCache.putIfAbsent(login, token);
    }

    public void removeToken(String login) {
        log.info("Removing token by login {}", login);
        tokenCache.remove(login);
    }
}
