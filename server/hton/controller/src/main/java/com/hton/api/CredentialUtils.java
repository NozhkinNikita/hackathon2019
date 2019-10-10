package com.hton.api;

import com.hton.config.UserDetails;
import com.hton.entities.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CredentialUtils {

    public String getCredentialLogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

    public List<Role> getCredentialRoles() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Role> roles = Collections.EMPTY_LIST;
        if (principal instanceof UserDetails) {
            roles = ((UserDetails)principal).getRoles();
        }
        return roles;
    }
}
