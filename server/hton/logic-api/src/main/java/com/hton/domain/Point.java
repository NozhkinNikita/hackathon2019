package com.hton.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {
    private String id;

    private String name;

    private LocalDateTime begin;

    private LocalDateTime end;

    private Boolean isRepeat;

    private String scanId;

    private List<RouterData> routerDates;
}
