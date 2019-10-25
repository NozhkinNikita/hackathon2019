package com.sb.wifistart.httprequests;

import com.sb.wifistart.dto.RouterData;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointUpdateRequest {
    private String id;

    private String begin;

    private String end;

    private List<RouterData> routerDatas;
}
