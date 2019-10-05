package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

//@Entity
//@Table(name = "ROUTER")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouterEntity {

    @Id
    private String id;

    private String ip;

    private String ssid;

    private String pwd;

    private String adminLogin;

    private String adminPwd;
}
