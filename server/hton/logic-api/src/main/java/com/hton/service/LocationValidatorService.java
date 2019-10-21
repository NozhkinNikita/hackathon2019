package com.hton.service;

import com.hton.domain.UserLocation;

import java.util.Optional;

public interface LocationValidatorService {
    Optional<UserLocation> validateLocation(String login, String locationId);
}
