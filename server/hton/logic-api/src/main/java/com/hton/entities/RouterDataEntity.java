package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "routerData")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouterDataEntity implements BaseEntity {
    @Id
    private String id;

    private String ssid;

    private String bssid;

    private Integer channel;

    private Double rssi;

    private String pointId;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = RouterEntity.class)
    @JoinColumn(name = "routerId")
    private RouterEntity ourRouter;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "ssid", "bssid", "canal", "rssi");
    }

    @Override
    public List<String> getJoinFields() {
        return Collections.singletonList("ourRouter");
    }
}
