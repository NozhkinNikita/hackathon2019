package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "scan")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanEntity implements BaseEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    
    private LocalDateTime begin;

    private LocalDateTime end;

    @Enumerated(EnumType.STRING)
    private ScanStatus status;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = UserLocationEntity.class)
//    @JoinTable(name = "user_location",
//            joinColumns = {@JoinColumn(name = "id")}
//    )
    @JoinColumn(name = "userLocationId")
    private UserLocationEntity userLocation;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = DeviceEntity.class, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "deviceId", nullable = false)
    private DeviceEntity device;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = PointEntity.class, mappedBy = "scanId")
    private List<PointEntity> points;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "begin", "end", "status");
    }

    @Override
    public List<String> getJoinFields() {
        return Arrays.asList("device", "points", "userLocation");
    }
}
