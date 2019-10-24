package com.sb.wifistart.httprequests;

import java.util.List;

public class LocationResponse {

    private String id;

    private String name;

    private List<String> routers;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getRouters() {
        return routers;
    }

}
