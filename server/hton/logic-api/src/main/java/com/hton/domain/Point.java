package com.hton.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Point {
    private String id;

    private String name;

    private LocalDateTime begin;

    private LocalDateTime end;

    private Boolean isRepeat;

    private String scanId;

    private List<RouterData> routerDates;
}
