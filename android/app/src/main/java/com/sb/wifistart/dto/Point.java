package com.sb.wifistart.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Point {

    private String id;
    private String name;
    private String begin;
    private String end;
    private Boolean isRepeat;
    private String scanId;
    private List<RouterData> routerDates;
}