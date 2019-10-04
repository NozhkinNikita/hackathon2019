package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity implements BaseEntity {

    @Id
    @Column(name = "id", unique = true)
    @Enumerated(EnumType.STRING)
    @Getter
    private Role id;

    @Override
    public List<String> getBaseFields() {
        return Collections.singletonList("id");
    }

    @Override
    public List<String> getJoinFields() {
        return new ArrayList<>(0);
    }

    public String getId() {
        return id.name();
    }
}
