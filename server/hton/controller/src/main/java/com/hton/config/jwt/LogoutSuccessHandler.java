package com.hton.config.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogoutSuccessHandler implements LogoutHandler {

    @Autowired
    private TokenCache tokenCache;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public void logout(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       Authentication authentication) {
        String token = httpServletRequest.getHeaders("Authorization").nextElement().substring(5);
        String login = jwtTokenUtil.getUsernameFromToken(token);
        tokenCache.removeToken(login);
    }
}
