package com.hton.api.auth;

import com.hton.entities.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserDetails extends User {

    @Getter
    private List<Role> roles;

    public UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
                       boolean credentialsNonExpired,
                       boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public UserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
                       boolean credentialsNonExpired,
                       boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, List<Role> roles) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.roles = roles;
    }
}
