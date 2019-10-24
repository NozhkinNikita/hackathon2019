package com.hton.api.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointCreateRequest {
    private String id;

    private String name;

    private String scanId;
}
