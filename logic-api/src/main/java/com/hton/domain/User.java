package com.hton.domain;

import com.hton.entities.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;

    private String fio;

    private String login;

    private String pwd;

    private List<RoleEntity> roleEntities;
}
