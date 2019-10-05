package com.hton.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

//@Entity
//@Table(name = "DEVICE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEntity {

    @Id
    private String id;

    private String model;

    private String softWareVersion;

    private String mac;

    private String ip;
}
