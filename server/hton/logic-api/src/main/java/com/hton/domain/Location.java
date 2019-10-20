package com.hton.domain;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private String id;

    private String name;

    private List<Router> routers;
}
