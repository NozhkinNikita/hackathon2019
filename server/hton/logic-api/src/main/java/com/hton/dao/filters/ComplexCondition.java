package com.hton.dao.filters;

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

public class ComplexCondition implements Condition, Serializable {
    private Operation operation;
    private Collection<Condition> conditions;
    private String sortField;
    private SortDirection sortDirection;
    private Integer skip;
    private Integer take;
    private List<String> maskFields;

    private ComplexCondition(final Operation operation, final Collection<Condition> conditions, final String sortField,
                             final SortDirection sortDirection,
                             final Integer skip, final Integer take, final List<String> maskFields) {
        this.operation = operation;
        this.conditions = conditions;
        this.sortField = StringUtils.isBlank(sortField) ? "id" : sortField;
        this.sortDirection = sortDirection == null ? SortDirection.ASC : sortDirection;
        this.skip = skip == null || skip < 0 ? DEFAULT_SKIP : skip;
        this.take = take == null || take <= 0 ? DEFAULT_TAKE : take > MAX_TAKE ? MAX_TAKE : take;
        this.maskFields = maskFields == null ? Collections.EMPTY_LIST : maskFields;
    }

    public Operation getOperation() {
        return operation;
    }

    public Collection<Condition> getConditions() {
        return conditions;
    }

    @Override
    public String getSortField() {
        return sortField;
    }

    @Override
    public SortDirection getSortDirection() {
        return sortDirection;
    }

    @Override
    public Integer getSkip() {
        return skip;
    }

    @Override
    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    @Override
    public Integer getTake() {
        return take;
    }

    @Override
    public void setTake(Integer take) {
        this.take = take;
    }

    @Override
    public List<String> getMaskFields() {
        return maskFields;
    }

    @Override
    public void setMaskFields(List<String> fields) {
        this.maskFields = fields;
    }

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ComplexCondition that = (ComplexCondition) o;

        if (operation != that.operation) return false;
        if (conditions != null ? !conditions.equals(that.conditions) : that.conditions != null) return false;
        if (sortField != null ? !sortField.equals(that.sortField) : that.sortField != null) return false;
        if (sortDirection != that.sortDirection) return false;
        if (skip != null ? !skip.equals(that.skip) : that.skip != null) return false;
        if (take != null ? !take.equals(that.take) : that.take != null) return false;
        return maskFields != null ? maskFields.equals(that.maskFields) : that.maskFields == null;
    }

    @Override
    public int hashCode() {
        int result = operation != null ? operation.hashCode() : 0;
        result = 31 * result + (conditions != null ? conditions.hashCode() : 0);
        result = 31 * result + (sortField != null ? sortField.hashCode() : 0);
        result = 31 * result + (sortDirection != null ? sortDirection.hashCode() : 0);
        result = 31 * result + (skip != null ? skip.hashCode() : 0);
        result = 31 * result + (take != null ? take.hashCode() : 0);
        result = 31 * result + (maskFields != null ? maskFields.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComplexCondition{" +
                "operation=" + operation +
                ", conditions=" + conditions +
                ", sortField='" + sortField + '\'' +
                ", sortDirection=" + sortDirection +
                ", skip=" + skip +
                ", take=" + take +
                ", maskFields=" + maskFields +
                '}';
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