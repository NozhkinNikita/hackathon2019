package com.hton.domain;

import lombok.Data;

@Data
public class Router {
    private String id;

    private String ip;

    private String ssid;

    private String pwd;

    private String adminLogin;

    private String adminPwd;

    private String locationId;
}
