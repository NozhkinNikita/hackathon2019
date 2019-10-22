package com.hton.api.requests;

import com.hton.domain.Device;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanCreateRequest {
    private String id;

    private LocalDateTime begin;

    private String locationId;

    private Device device;
}
