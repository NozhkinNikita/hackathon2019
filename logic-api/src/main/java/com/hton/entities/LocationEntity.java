package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

//@Entity
//@Table(name = "LOCATION")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationEntity {

    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "location_id")
    private List<RouterEntity> routers;
}
