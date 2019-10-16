package com.hton.dao.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplexCondition implements Condition, Serializable {
    private Operation operation;
    private Collection<Condition> conditions;
    private String sortField;
    private SortDirection sortDirection;
    private Integer skip;
    private Integer take;
    private List<String> maskFields;

    @Override
    public Predicate getPredicate(CriteriaBuilder criteriaBuilder, Root root, AbstractQuery criteriaQuery) {
        List<Predicate> predicates = new ArrayList<>();
        switch (operation) {
            case AND: {
                conditions.forEach(query -> predicates.add(query.getPredicate(criteriaBuilder, root, criteriaQuery)));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
            case OR: {
                conditions.forEach(query -> predicates.add(query.getPredicate(criteriaBuilder, root, criteriaQuery)));
                return criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]));
            }
            default: {
                throw new IllegalArgumentException(String.format("Предикат запроса %s не поддерживается", operation));
            }
        }
    }

    public static class Builder {
        private Operation operation;
        private Collection<Condition> conditions;
        private String sortField;
        private SortDirection sortDirection;
        private Integer skip;
        private Integer take;
        private List<String> maskFields;

        public Builder setOperation(Operation operation) {
            this.operation = operation;
            return this;
        }

        public Builder setConditions(Collection<Condition> conditions) {
            this.conditions = conditions;
            return this;
        }

        public Builder setConditions(Condition... conditions) {
            this.conditions = Arrays.stream(conditions).collect(Collectors.toList());
            return this;
        }

        public Builder setQueries(Condition... queries) {
            this.conditions = Arrays.asList(queries);
            return this;
        }

        public Builder setQueries(List<Condition> queries) {
            this.conditions = queries;
            return this;
        }

        public Builder setSortField(String sortField) {
            this.sortField = sortField;
            return this;
        }

        public Builder setSortDirection(SortDirection sortDirection) {
            this.sortDirection = sortDirection;
            return this;
        }

        public Builder setSkip(Integer skip) {
            this.skip = skip;
            return this;
        }

        public Builder setTake(Integer take) {
            this.take = take;
            return this;
        }

        public Builder setMaskFields(List<String> maskFields) {
            this.maskFields = maskFields;
            return this;
        }

        public ComplexCondition build() {
            return new ComplexCondition(operation, conditions, sortField, sortDirection, skip, take, maskFields);
        }
    }
}