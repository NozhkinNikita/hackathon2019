package com.hton.domain;

import lombok.Data;

@Data
public class RouterData {
    private String id;

    private String ssid;

    private String bssid;

    private Integer channel;

    private Double rssi;

    private Router ourRouter;
}
