package com.hton.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hton.dao.filters.ComplexCondition;
import com.hton.dao.filters.Condition;
import com.hton.dao.filters.Operation;
import com.hton.dao.filters.SearchCondition;
import com.hton.dao.filters.SimpleCondition;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class FilterUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Condition parseFilter(String filter) {
        Condition condition;
        if (StringUtils.isBlank(filter)) {
            condition = new SimpleCondition.Builder().setSearchField("id")
                    .setSearchCondition(SearchCondition.NOT_NULL).build();
        } else {
            try {
                condition = objectMapper.readValue(filter, Condition.class);
            } catch (IOException e) {
                throw new IllegalArgumentException("Cannot parse filter: " + filter);
            }
        }
        return condition;
    }

    public static Condition getFilterWithLogin(String filter, String login) {
        Condition condition = FilterUtils.parseFilter(filter);

        SimpleCondition loginCondition = new SimpleCondition.Builder()
                .setSearchField("users.login")
                .setSearchCondition(SearchCondition.EQUALS)
                .setSearchValue(login)
                .build();
        return new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(condition, loginCondition)
                .build();
    }
}
