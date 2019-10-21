package com.hton.api.requests;

import com.hton.domain.Location;
import com.hton.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateRequest {
    private List<User> users;
    private Location locaiotn;
}
