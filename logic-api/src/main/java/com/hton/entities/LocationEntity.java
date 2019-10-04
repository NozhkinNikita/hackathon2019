package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "location")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationEntity implements BaseEntity {

    @Id
    private String id;

    private String name;

//    @OneToMany(mappedBy = "location_id")
//    private List<RouterEntity> routers;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "name");
    }

    @Override
    public List<String> getJoinFields() {
        return new ArrayList<>();
    }
}
