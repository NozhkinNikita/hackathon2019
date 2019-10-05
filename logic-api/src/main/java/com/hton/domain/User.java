package com.hton.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hton.entities.Role;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "locations")
@ToString(exclude = "locations")
public class User {

    private String id;

    private String fio;

    private String login;

    @Setter(onMethod = @__(@JsonProperty))
    @Getter(onMethod = @__(@JsonIgnore))
    private String pwd;

    private List<Role> roles;

    private List<Location> locations;
}
