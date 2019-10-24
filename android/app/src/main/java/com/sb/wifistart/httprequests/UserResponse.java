package com.sb.wifistart.httprequests;

import java.util.List;

public class UserResponse {

    private String login;

    private Boolean enabled;

    private List<String> roles;

    private String token;

    public String getLogin() {
        return login;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getToken() {
        return token;
    }
}
