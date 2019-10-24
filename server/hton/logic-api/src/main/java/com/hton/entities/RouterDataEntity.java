package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "routerData")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouterDataEntity implements BaseEntity {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    private String ssid;

    private String bssid;

    private Integer channel;

    private Double rssi;

    private String pointId;

    private String ourRouterId;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "ssid", "bssid", "canal", "rssi", "ourRouterId");
    }

    @Override
    public List<String> getJoinFields() {
        return new ArrayList<>(0);
    }
}
