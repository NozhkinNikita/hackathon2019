package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements BaseEntity {
    @Id
    private String id;

    private String fio;

    @Column(unique = true)
    private String login;

    private String pwd;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "userId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "roleId", nullable = false, updatable = false)})
    private List<RoleEntity> roles;
//
//    @OneToMany(mappedBy = "user_id")
//    private List<LocationEntity> locations;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "fio", "login", "pwd");
    }

    @Override
    public List<String> getJoinFields() {
        return Collections.singletonList("roleEntities");
    }
}
