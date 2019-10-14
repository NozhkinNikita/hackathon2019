package com.hton.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private String jsonPassword;
    private String jsonUsername;

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String password;

        if ("application/json;charset=UTF-8".equals(request.getHeader("Content-Type")) ||
                "application/json".equals(request.getHeader("Content-Type"))) {
            password = this.jsonPassword;
        } else {
            password = super.obtainPassword(request);
        }

        return password;
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username;

        if ("application/json;charset=UTF-8".equals(request.getHeader("Content-Type")) ||
                "application/json".equals(request.getHeader("Content-Type"))) {
            username = this.jsonUsername;
        } else {
            username = super.obtainUsername(request);
        }

        return username;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if ("application/json;charset=UTF-8".equals(request.getHeader("Content-Type")) ||
                "application/json".equals(request.getHeader("Content-Type"))) {
            try {
                /*
                 * HttpServletRequest can be read only once
                 */
                StringBuilder sb = new StringBuilder();
                String line;

                BufferedReader reader = request.getReader();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                //json transformation
                ObjectMapper mapper = new ObjectMapper();
                LoginRequest loginRequest = mapper.readValue(sb.toString(), LoginRequest.class);

                this.jsonUsername = loginRequest.getUsername();
                this.jsonPassword = loginRequest.getPassword();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.attemptAuthentication(request, response);
    }
}
