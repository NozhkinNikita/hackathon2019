package com.hton.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocation {

    private String id;

    private User user;

    private Location location;

    private Boolean actualRelation;
}
