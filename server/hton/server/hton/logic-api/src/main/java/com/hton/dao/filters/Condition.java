package com.hton.dao.filters;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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
