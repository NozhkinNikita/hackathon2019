package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.OneToOne;

//@Entity
//@Table(name = "ROUTER_DATA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouterDataEntity {
    @Id
    private String id;

    private String ssid;

    private String bssid;

    private Integer canal;

    private Double rssi;

    @OneToOne(mappedBy = "router_id")
    private RouterEntity ourRouter;
}
