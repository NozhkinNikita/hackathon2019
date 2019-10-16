package com.hton.dao.filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ComplexCondition.class, name = "ComplexCondition"),

        @JsonSubTypes.Type(value = SimpleCondition.class, name = "SimpleCondition") }
)

/*
  Json example:
  {"@type":"ComplexCondition","operation":"AND","conditions":[
       {"@type":"SimpleCondition","searchField":"field1","searchCondition":"EQUALS","searchValue":"asd1",
            "sortField":null,"sortDirection":null,"skip":null,"take":null,"maskFields":null},
       {"@type":"SimpleCondition","searchField":"field2","searchCondition":"EQUALS","searchValue":"asd2",
            "sortField":null,"sortDirection":null,"skip":null,"take":null,"maskFields":null}],
  "sortField":null,"sortDirection":null,"skip":null,"take":null,"maskFields":null}
  */
public interface Condition {
    Integer DEFAULT_TAKE = -1;
    Integer MAX_TAKE = 100;
    Integer DEFAULT_SKIP = 0;

    Predicate getPredicate(CriteriaBuilder criteriaBuilder, Root root, AbstractQuery criteriaQuery);

    Integer getSkip();

    void setSkip(Integer skip);

    Integer getTake();

    void setTake(Integer take);

    List<String> getMaskFields();

    void setMaskFields(List<String> fields);

    SortDirection getSortDirection();

    String getSortField();
}
