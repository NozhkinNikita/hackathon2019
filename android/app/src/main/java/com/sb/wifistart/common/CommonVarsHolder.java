package com.sb.wifistart.common;

import com.sb.wifistart.dto.Point;

import java.util.List;

import com.sb.wifistart.httprequests.UserResponse;

public class CommonVarsHolder {

    public static String locationName;
    public static String locationStartDate;
    public static List<Point> currentPoints;
    public static String scanId;
    public static UserResponse userResponse;

    private CommonVarsHolder() {

    }
}
