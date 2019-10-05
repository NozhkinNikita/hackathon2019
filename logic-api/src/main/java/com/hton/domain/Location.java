package com.hton.domain;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
public class Location {
    private String id;

    private String name;

    private List<User> users;
}
