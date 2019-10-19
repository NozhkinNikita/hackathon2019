package com.hton.domain;

import lombok.*;

import java.util.List;

@Data
public class Location {
    private String id;

    private String name;

    private List<Router> routers;
}
