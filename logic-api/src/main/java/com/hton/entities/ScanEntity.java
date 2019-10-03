package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.List;

//@Entity
//@Table(name = "SCAN")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanEntity {
    @Id
    private String id;

    private LocalDateTime begin;

    private LocalDateTime end;

    private ScanStatus status;

    private UserEntity user;

    @OneToOne(mappedBy = "device_id")
    private DeviceEntity device;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "point_id")
    private List<PointEntity> points;
}
