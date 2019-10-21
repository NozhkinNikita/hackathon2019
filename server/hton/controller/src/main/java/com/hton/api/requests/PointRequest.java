package com.hton.api.requests;

import com.hton.domain.RouterData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointRequest {
    private String id;

    private String name;

    private LocalDateTime begin;

    private LocalDateTime end;

    private String scanId;

    private List<RouterData> routerDatas;
}
