package com.hton.domain;

import com.hton.entities.ScanStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Scan {
    private String id;

    private LocalDateTime begin;

    private LocalDateTime end;

    private ScanStatus status;

    private UserLocation userLocation;

    private Device device;

    private List<Point> points;
}
