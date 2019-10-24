package com.sb.wifistart.httprequests;

import com.sb.wifistart.dto.Device;

public class CreateScanRequest {
    private String locationId;

    private Device device;

    public CreateScanRequest(String locationId, Device device) {
        this.locationId = locationId;
        this.device = device;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
