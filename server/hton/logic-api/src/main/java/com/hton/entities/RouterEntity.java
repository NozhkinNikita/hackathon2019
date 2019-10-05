package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "router")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouterEntity implements BaseEntity {

    @Id
    private String id;

    private String ip;

    private String ssid;

    private String pwd;

    private String adminLogin;

    private String adminPwd;

    private String locationId;

    @Override
    public List<String> getBaseFields() {
        return Arrays.asList("id", "ip", "ssid", "pwd", "adminLogin", "adminPwd", "locationId");
    }

    @Override
    public List<String> getJoinFields() {
        return new ArrayList<>(0);
    }
}
