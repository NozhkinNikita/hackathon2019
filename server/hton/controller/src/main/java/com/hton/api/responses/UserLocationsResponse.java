package com.hton.api.responses;

import com.hton.domain.Location;
import com.hton.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLocationsResponse {

    private User user;

    private List<String> locationIds;
}
