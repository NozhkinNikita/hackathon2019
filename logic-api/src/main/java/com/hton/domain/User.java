package com.hton.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hton.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    private String id;

    private String fio;

    private String login;

    @Setter(onMethod = @__(@JsonProperty))
    @Getter(onMethod = @__(@JsonIgnore))
    private String pwd;

    private List<Role> roles;
}
