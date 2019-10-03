package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
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

//    @ElementCollection
//    @Enumerated(EnumType.STRING)
//    private List<Role> roles;
//
//    @OneToMany(mappedBy = "user_id")
//    private List<LocationEntity> locations;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "fio", "login", pwd);
    }

    @Override
    public List<String> getJoinFields() {
        return new ArrayList<>();
    }
}
