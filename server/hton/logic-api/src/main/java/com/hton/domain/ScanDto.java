package com.hton.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScanDto {
    private String id;

    private LocalDateTime begin;

    private String locationId;

    private Device device;
}
