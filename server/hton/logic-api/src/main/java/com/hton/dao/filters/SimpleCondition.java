package com.hton.dao.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleCondition implements Condition, Serializable {
    private String searchField;
    private SearchCondition searchCondition;
    private Object searchValue;
    private String sortField;
    private SortDirection sortDirection;
    private Integer skip;
    private Integer take;
    private List<String> maskFields;

    @Override
    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("uncheckecd")
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
