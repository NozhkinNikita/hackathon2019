package com.sb.wifistart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouterData {

    private String id;
    private String ssid;
    private String bssid;
    private Integer channel;
    private Double rssi;
    private String pointId;
    private Router ourRouter;
}
