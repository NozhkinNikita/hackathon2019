package com.hton.entities;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "locations")
@ToString(exclude = "locations")
public class UserEntity implements BaseEntity {
    @Id
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

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = LocationEntity.class)
    @JoinTable(name = "user_location",
            joinColumns = {@JoinColumn(name = "userId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "locationId", nullable = false, updatable = false)})
    private List<LocationEntity> locations;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "fio", "login", "pwd");
    }

    @Override
    public List<String> getJoinFields() {
        return Arrays.asList("roleEntities", "locations");
    }
}
