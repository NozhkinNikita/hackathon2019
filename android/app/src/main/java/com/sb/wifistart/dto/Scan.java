package com.sb.wifistart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Scan {

    private String id;
    private LocalDateTime begin;
    private LocalDateTime end;
    private ScanStatus status;

    //    ToDo Uncomment in future
    //    private User user;
    private Device device;
    private List<Point> points;
}