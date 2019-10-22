package com.hton.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Router {
    private String id;

    private String ip;

    private String ssid;

    private String pwd;

    private String adminLogin;

    private String adminPwd;

    private String locationId;
}
