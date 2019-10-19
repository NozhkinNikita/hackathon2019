package com.hton.api.auth;

import com.hton.config.UserDetails;
import com.hton.entities.Role;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {

    private String login;

    private Boolean enabled;

    private List<Role> roles;

    private String token;

    public LoginResponse(UserDetails user, String token) {
        this.login = user.getUsername();
        this.roles = user.getRoles();
        this.enabled = user.isEnabled();
        this.token = token;
    }
}
