package com.hton.domain;

import lombok.Data;

@Data
public class UserLocation {

    private String id;

    private User user;

    private Location location;
}
