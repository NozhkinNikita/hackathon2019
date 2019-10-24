package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements BaseEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String fio;

    @Column(unique = true)
    private String login;

    private String pwd;

    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "userId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "roleId", nullable = false, updatable = false)})
    @Fetch(value = FetchMode.SUBSELECT)
    private List<RoleEntity> roles;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "fio", "login", "pwd", "enabled");
    }

    @Override
    public List<String> getJoinFields() {
        return Arrays.asList("roles");
    }
}
