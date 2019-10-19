package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "device")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEntity implements BaseEntity {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String model;

    private String osVersion;

    private String manufacturer;

    private String brand;

    private String deviceId;

    private String device;

    private String mac;

    private String ipV4;

    private String release;

    private String product;

    private String serial;

    private String user;

    private String host;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "model", "osVersion", "manufacturer", "brand", "deviceId", "device", "mac", "ipV4",
                "release", "product", "serial", "user", "host");
    }

    @Override
    public List<String> getJoinFields() {
        return new ArrayList<>(0);
    }
}
