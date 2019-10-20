package com.hton.service;

import com.hton.dao.CommonDao;
import com.hton.dao.filters.ComplexCondition;
import com.hton.dao.filters.Operation;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import com.hton.domain.UserLocation;
import com.hton.entities.UserLocationEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class LocationValidatorServiceImpl implements LocationValidatorService {

    @Autowired
    private CommonDao<UserLocation, UserLocationEntity> userLocationDao;

    @Override
    public Optional<UserLocation> validateLocation(String login, String locationId) {
        ComplexCondition condition = new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(new SimpleCondition.Builder()
                                .setSearchField("location.id")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(locationId)
                                .build(),
                        new SimpleCondition.Builder()
                                .setSearchField("user.login")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(login)
                                .build()
                )
                .build();

        List<UserLocation> userLocations = userLocationDao.getByCondition(condition);

        if (userLocations.isEmpty()) {
            log.warn(String.format("Пользователь с логином %s пытался выполнить действие в неразрешенной локации с id: %s", login, locationId));
            return Optional.empty();
        } else {
            return Optional.of(userLocations.get(0));
        }
    }
}
