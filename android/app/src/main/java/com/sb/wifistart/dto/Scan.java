package com.sb.wifistart.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Scan implements Serializable {

    private String id;
    private String begin;
    private String end;
    private ScanStatus status;

    //    ToDo Uncomment in future
    //    private User user;
    private Device device;
    private List<Point> points;
}