package com.hton.api;

import com.hton.dao.filters.ComplexCondition;
import com.hton.dao.filters.Condition;
import com.hton.dao.filters.Operation;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;

import java.util.ArrayList;
import java.util.List;

public class UserLocationConditionHelper {

    public static Condition getUserLocationCondition(String userId, String locationId, List<String> mask) {
        return  new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(
                        new SimpleCondition.Builder()
                                .setSearchField("userId")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(userId)
                                .build(),
                        new SimpleCondition.Builder()
                                .setSearchField("locationId")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue(locationId)
                                .build()
                )
                .setMaskFields(mask == null ? new ArrayList<>(0) : mask)
                .build();
    }
}
