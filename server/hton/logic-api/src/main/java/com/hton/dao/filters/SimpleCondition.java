package com.hton.dao.filters;

import org.apache.commons.lang.StringUtils;
import org.hibernate.query.criteria.internal.path.ListAttributeJoin;
import org.hibernate.query.criteria.internal.path.SingularAttributeJoin;

import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class SimpleCondition implements Condition, Serializable {
    private String searchField;
    private SearchCondition searchCondition;
    private Object searchValue;
    private String sortField;
    private SortDirection sortDirection;
    private Integer skip;
    private Integer take;
    private List<String> maskFields;

    private SimpleCondition(String searchField, SearchCondition searchCondition, Object searchValue, String sortField, SortDirection sortDirection,
                            Integer skip, Integer take, List<String> maskFields) {
        this.searchField = searchField;
        this.searchCondition = searchCondition;
        this.searchValue = searchValue;
        this.sortField = StringUtils.isBlank(sortField) ? "id" : sortField;
        this.sortDirection = sortDirection == null ? SortDirection.ASC : sortDirection;
        this.skip = skip == null || skip < 0 ? DEFAULT_SKIP : skip;
        this.take = take == null || take <= 0 ? DEFAULT_TAKE : take > MAX_TAKE ? MAX_TAKE : take;
        this.maskFields = maskFields == null ? Collections.EMPTY_LIST : maskFields;
    }

    public String getSearchField() {
        return searchField;
    }

    public SearchCondition getSearchCondition() {
        return searchCondition;
    }

    public Object getSearchValue() {
        return searchValue;
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
        String[] search = searchField.split("\\.");
        if (search.length > 1) {
            Join join = (Join) root.getJoins().stream()
                    .filter(j -> {
                        Class joinClasss = j.getClass();
                        if (joinClasss.equals(ListAttributeJoin.class)) {
                            return ((ListAttributeJoin) j).getAttribute().getName().equals(search[0]);
                        } else if (joinClasss.equals(SingularAttributeJoin.class)) {
                            return ((SingularAttributeJoin) j).getAttribute().getName().equals(search[0]);
                        } else {
                            return false;
                        }
                    }).findFirst().orElseGet(() -> root.join(search[0]));
            return getPredicate(criteriaBuilder, criteriaQuery, join, search[1]);
        } else {
            return getPredicate(criteriaBuilder, criteriaQuery, root, searchField);
        }
    }

    private Predicate getPredicate(CriteriaBuilder criteriaBuilder, AbstractQuery criteriaQuery, From from, String queryField) {
        switch (searchCondition) {
            case NULL: {
                return criteriaBuilder.isNull(from.get(queryField));
            }
            case NOT_NULL: {
                return criteriaBuilder.isNotNull(from.get(queryField));
            }
            case EQUALS: {
                return criteriaBuilder.equal(from.get(queryField), searchValue);
            }
            case NOT_EQUALS: {
                return criteriaBuilder.notEqual(from.get(queryField), searchValue);
            }
            case LIKE: {
                return criteriaBuilder.like(from.get(queryField), "%" + searchValue.toString() + "%");
            }
            case NOT_LIKE: {
                return criteriaBuilder.notLike(from.get(queryField), "%" + searchValue.toString() + "%");
            }
            case GREATER_THAN: {
                return criteriaBuilder.greaterThan(from.get(queryField), (Comparable) searchValue);
            }
            case GREATER_OR_EQUAL_THAN: {
                return criteriaBuilder.greaterThanOrEqualTo(from.get(queryField), (Comparable) searchValue);
            }
            case LESS_THAN: {
                return criteriaBuilder.lessThan(from.get(queryField), (Comparable) searchValue);
            }
            case LESS_OR_EQUAL_THAN: {
                return criteriaBuilder.lessThanOrEqualTo(from.get(queryField), (Comparable) searchValue);
            }
            case MAX: {
                checkQuery();
                Subquery subQuery;
                Class entityClass = from.getJavaType();
                try {
                    subQuery = criteriaQuery.subquery(entityClass.getDeclaredField(queryField).getType());
                } catch (NoSuchFieldException e) {
                    throw new IllegalArgumentException(String.format("Неверно указано поле %s", queryField));
                }
                Root subRoot = subQuery.from(entityClass);
                subQuery.select(criteriaBuilder.greatest(subRoot.get(queryField)));
                if (searchValue != null) {
                    Condition innerQuery = (Condition) searchValue;
                    subQuery.where(innerQuery.getPredicate(criteriaBuilder, subRoot, subQuery));
                }

                return criteriaBuilder.equal(from.get(queryField), subQuery);
            }
            case MIN: {
                checkQuery();
                Subquery subQuery;
                Class entityClass = from.getJavaType();
                try {
                    subQuery = criteriaQuery.subquery(entityClass.getDeclaredField(queryField).getType());
                } catch (NoSuchFieldException e) {
                    throw new IllegalArgumentException(String.format("Неверно указано поле %s", queryField));
                }
                Root subRoot = subQuery.from(entityClass);
                subQuery.select(criteriaBuilder.least(subRoot.get(queryField)));
                if (searchValue != null) {
                    Condition innerQuery = (Condition) searchValue;
                    subQuery.where(innerQuery.getPredicate(criteriaBuilder, subRoot, subQuery));
                }

                return criteriaBuilder.equal(from.get(queryField), subQuery);
            }
            default: {
                throw new IllegalArgumentException(String.format("SearchCondition %s не поддерживается", searchCondition));
            }
        }
    }

    private void checkQuery() {
        if (searchValue != null && searchValue.getClass().getSuperclass() == Condition.class) {
            throw new IllegalArgumentException(String.format("Неверно создан запрос %s", this));
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final SimpleCondition condition = (SimpleCondition) o;

        if (searchField != null ? !searchField.equals(condition.searchField) : condition.searchField != null) return false;
        if (searchCondition != condition.searchCondition) return false;
        if (searchValue != null ? !searchValue.equals(condition.searchValue) : condition.searchValue != null) return false;
        if (sortField != null ? !sortField.equals(condition.sortField) : condition.sortField != null) return false;
        if (sortDirection != condition.sortDirection) return false;
        if (skip != null ? !skip.equals(condition.skip) : condition.skip != null) return false;
        if (take != null ? !take.equals(condition.take) : condition.take != null) return false;
        return maskFields != null ? maskFields.equals(condition.maskFields) : condition.maskFields == null;
    }

    @Override
    public int hashCode() {
        int result = searchField != null ? searchField.hashCode() : 0;
        result = 31 * result + (searchCondition != null ? searchCondition.hashCode() : 0);
        result = 31 * result + (searchValue != null ? searchValue.hashCode() : 0);
        result = 31 * result + (sortField != null ? sortField.hashCode() : 0);
        result = 31 * result + (sortDirection != null ? sortDirection.hashCode() : 0);
        result = 31 * result + (skip != null ? skip.hashCode() : 0);
        result = 31 * result + (take != null ? take.hashCode() : 0);
        result = 31 * result + (maskFields != null ? maskFields.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleCondition{" +
                "searchField='" + searchField + '\'' +
                ", searchCondition=" + searchCondition +
                ", searchValue=" + searchValue +
                ", sortField='" + sortField + '\'' +
                ", sortDirection=" + sortDirection +
                '}';
    }

    public static class Builder {
        private String searchField;
        private SearchCondition searchCondition;
        private Object searchValue;
        private String sortField;
        private SortDirection sortDirection;
        private Integer skip;
        private Integer take;
        private List<String> maskFields;

        public Builder setSearchField(String searchField) {
            this.searchField = searchField;
            return this;
        }

        public Builder setSearchCondition(SearchCondition searchCondition) {
            this.searchCondition = searchCondition;
            return this;
        }

        public Builder setSearchValue(Object searchValue) {
            this.searchValue = searchValue;
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

        public SimpleCondition build() {
            return new SimpleCondition(searchField, searchCondition, searchValue, sortField, sortDirection, skip, take, maskFields);
        }
    }
}
