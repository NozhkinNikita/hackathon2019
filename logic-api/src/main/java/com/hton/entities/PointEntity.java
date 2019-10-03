package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

//@Entity
//@Table(name = "POINT")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointEntity {

    @Id
    private String id;

    private String name;

    private LocalDateTime begin;

    private LocalDateTime end;

    private Boolean isRepeat;

    @OneToMany(mappedBy = "router_data_id")
    private List<RouterDataEntity> routerDates;
}
