package com.hton.dao.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
public class ConditionTest {


    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void test() throws IOException {
        ComplexCondition condition = new ComplexCondition.Builder()
                .setOperation(Operation.AND)
                .setConditions(
                        new SimpleCondition.Builder()
                                .setSearchField("field1")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue("asd1")
                                .build(),
                        new SimpleCondition.Builder()
                                .setSearchField("field2")
                                .setSearchCondition(SearchCondition.EQUALS)
                                .setSearchValue("asd2")
                                .build()
                )
                .build();

        String json = objectMapper.writeValueAsString(condition);
        System.out.println("json = " + json);
        Condition result = objectMapper.readValue(json, Condition.class);
        System.out.println(":::::::::::::::::::::::::::::::::::::::");
        System.out.println("result = " + result);
        System.out.println(":::::::::::::::::::::::::::::::::::::::");

        String json2 = "{\"@type\":\"ComplexCondition\",\"operation\":\"AND\",\"conditions\":[" +
        "{\"@type\":\"SimpleCondition\",\"searchField\":\"field1\",\"searchCondition\":\"EQUALS\",\"searchValue\":\"asd1\"}," +
//                "sortField\":null,\"sortDirection\":null,\"skip\":null,\"take\":null,\"maskFields\":null}," +
        "{\"@type\":\"SimpleCondition\",\"searchField\":\"field2\",\"searchCondition\":\"EQUALS\",\"searchValue\":\"asd2\"}]}"
//                "sortField\":null,\"sortDirection\":null,\"skip\":null,\"take\":null,\"maskFields\":null}]," +
        /*"sortField\":null,\"sortDirection\":null,\"skip\":null,\"take\":null,\"maskFields\":null}"*/;

        Condition res2 = objectMapper.readValue(json2, Condition.class);
        System.out.println(":::::::::::::::::::::::::::::::::::::::");
        System.out.println("resul2 = " + res2);
        System.out.println(":::::::::::::::::::::::::::::::::::::::");

    }

}
