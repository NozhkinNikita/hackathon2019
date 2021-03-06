package com.hton.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
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
}
