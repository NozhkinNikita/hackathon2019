package com.hton.dao;

import com.hton.converters.Converter;
import com.hton.dao.filters.Condition;

import com.hton.entities.BaseEntity;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.criteria.internal.PathSource;
import org.hibernate.query.criteria.internal.path.AbstractPathImpl;
import org.hibernate.query.criteria.internal.path.ListAttributeJoin;
import org.hibernate.query.criteria.internal.path.RootImpl;
import org.hibernate.query.criteria.internal.path.SingularAttributeJoin;
import org.hibernate.query.criteria.internal.path.SingularAttributePath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceUnit;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public abstract class CommonDao<D, E extends BaseEntity> {

    public abstract Class<E> getEntityClass();

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    private Converter<D, E> converter;

    public D getById(String id) {
        EntityManager em = emf.createEntityManager();
        E e = em.find(getEntityClass(), id);
        return converter.toDomainObject(e);
    }

    public E getById(String id, EntityManager entityManager) {
        E t = entityManager.find(getEntityClass(), id);
        return t;
    }

    public E getById(String id, EntityManager entityManager, LockModeType lockModeType) {
        E t = entityManager.find(getEntityClass(), id, lockModeType);
        return t;
    }

    public E getByIdOrNull(String id, EntityManager entityManager) {
        return entityManager.find(getEntityClass(), id);
    }

    public List<E> getByCondition(Condition condition) {
        return executeCondition(condition, null);
    }

    public List<E> getByCondition(Condition condition, EntityManager entityManager) {
        return executeCondition(condition, entityManager);
    }

    public Long count(Condition condition) {
        return count(condition, null);
    }

    public Long count(Condition condition, EntityManager entityManager) {
        EntityManager manager = emf.createEntityManager();
        condition.setMaskFields(Collections.singletonList("id"));

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Class<E> entityClass = getEntityClass();
        Root<E> parentRoot = criteriaQuery.from(entityClass);
        criteriaQuery.select(criteriaBuilder.count(parentRoot))
                .where(condition.getPredicate(criteriaBuilder, parentRoot, criteriaQuery));
        return manager.createQuery(criteriaQuery).getSingleResult();
    }

    private List<E> executeCondition(Condition condition, EntityManager entityManager) {

        CriteriaBuilder criteriaBuilder = emf.getCriteriaBuilder();
        Class<E> entityClass = getEntityClass();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<E> parentRoot = criteriaQuery.from(entityClass);
        List<Selection> selections = getSelections(condition, parentRoot);

        EntityManager outerEntityManager = entityManager == null ? emf.createEntityManager() : entityManager;
        List<E> result = new ArrayList<>(0);
        try {
            TypedQuery<Tuple> typedQuery = createQuery(parentRoot, criteriaBuilder, criteriaQuery, selections, condition, outerEntityManager);
            List<Tuple> queryResults = typedQuery.getResultList();
            result.addAll(parseResults(queryResults, entityClass));
        } finally {
            if (entityManager == null) {
                outerEntityManager.close();
            }
        }
        return result;
    }

    private TypedQuery<Tuple> createQuery(Root<E> parentRoot, CriteriaBuilder criteriaBuilder, CriteriaQuery<Tuple> criteriaQuery,
                                          List<Selection> selections, Condition condition, EntityManager outerEntityManager) {
        Order order = getOrder(parentRoot, criteriaBuilder, condition);
        TypedQuery<Tuple> typedQuery;
        if (parentRoot.getJoins().isEmpty()) {
            criteriaQuery.multiselect(selections.toArray(new Selection[selections.size()]))
                    .where(condition.getPredicate(criteriaBuilder, parentRoot, criteriaQuery)).orderBy(order);
            typedQuery = outerEntityManager.createQuery(criteriaQuery).setFirstResult(condition.getSkip());

            Integer take = condition.getTake();
            if (take != null && take > 0) {
                typedQuery.setMaxResults(take);
            }
        } else {
            criteriaQuery.multiselect(selections.toArray(new Selection[selections.size()]))
                    .where(condition.getPredicate(criteriaBuilder, parentRoot, criteriaQuery)).orderBy(order);
            typedQuery = outerEntityManager.createQuery(criteriaQuery);
        }
        return typedQuery;
    }

    private Order getOrder(Root<E> parentRoot, CriteriaBuilder criteriaBuilder, Condition condition) {
        Order order;
        String[] sortBy = condition.getSortField().split("\\.");
        boolean isJoinSort = sortBy.length > 1;
        String sortField = isJoinSort ? sortBy[sortBy.length - 1] : condition.getSortField();
        From orderBy = isJoinSort ? parentRoot.getJoins().stream().filter(join -> join.getAttribute().getName().equals(sortBy[0]))
                .findFirst().orElseGet(() -> parentRoot.join(sortBy[0], JoinType.LEFT)) : parentRoot;
        switch (condition.getSortDirection()) {
            case ASC: {
                order = criteriaBuilder.asc(orderBy.get(sortField));
                break;
            }
            case DESC: {
                order = criteriaBuilder.desc(orderBy.get(sortField));
                break;
            }
            default: {
                throw new IllegalArgumentException(String.format("Сортировка %s не поддерживается", condition.getSortDirection()));
            }
        }
        return order;
    }

    private List<E> parseResults(List<Tuple> queryResults, Class entityClass) {
        Map<String, E> result = new LinkedHashMap<>();
        // rootId -> joinRootClass -> joinRootId -> rootFieldName -> joinId -> joinValue
        Map<String, Map<Class, Map<String, Map<String, Map<String, BaseEntity>>>>> joins = new LinkedHashMap<>();

        queryResults.forEach(tuple -> {
            Optional<TupleElement<?>> idElement = tuple.getElements().stream().filter(tupleElement -> filterElementsToId(tupleElement, entityClass))
                    .findFirst();
            String elementId = null;
            if (idElement.isPresent()) {
                elementId = String.valueOf(tuple.get(tuple.getElements().indexOf(idElement.get())));
            }
            E resultObject = elementId != null && result.containsKey(elementId) ? result.get(elementId) : (E) createClassInstance(entityClass);

            tuple.getElements().forEach(element -> {
                String parameterName;
                Object elementValue = tuple.get(tuple.getElements().indexOf(element));
                if (elementValue == null) {
                    return;
                }

                if (element.getClass().equals(SingularAttributePath.class)) {
                    getTupleJoin(tuple, element, entityClass, resultObject, elementValue, joins, SingularAttributePath.class);
                    if (resultObject.getId() != null) {
                        result.putIfAbsent(resultObject.getId(), resultObject);
                    }
                } else if (element.getClass().equals(ListAttributeJoin.class)) {
                    parameterName = ((ListAttributeJoin) element).getAttribute().getName();
                    setFieldValue(resultObject, parameterName, elementValue);
                    if (resultObject.getId() != null) {
                        result.putIfAbsent(resultObject.getId(), resultObject);
                    }
                } else if (element.getClass().equals(RootImpl.class)) {
                    String id = String.valueOf(getFieldValue(elementValue));
                    if (StringUtils.isNotBlank(id)) {
                        result.putIfAbsent(id, (E) elementValue);
                    }
                } else if (element.getClass().equals(SingularAttributeJoin.class)) {
                    getTupleJoin(tuple, element, entityClass, resultObject, elementValue, joins, SingularAttributeJoin.class);
                    if (resultObject.getId() != null) {
                        result.putIfAbsent(resultObject.getId(), resultObject);
                    }
                } else {
                    throw new IllegalArgumentException(String.format("Тип %s возвращаемого объекта не поддерживается", element.getClass()));
                }
            });
        });
        // rootId -> joinRootClass -> joinRootId -> rootFieldName -> joinId -> joinValue
        joins.forEach((entityId, mapJoinsByJoinRootClass) -> {
            Object resultObject = result.get(entityId);
            mapJoinsByJoinRootClass.forEach((joinRootClass, mapJoinsByJoinRootId) -> {
                if (joinRootClass.equals(resultObject.getClass())) {
                    mapJoinsByJoinRootId.forEach((rootId, mapJoinsByRootFieldName) ->
                            mapJoinsByRootFieldName.forEach((rootFieldName, mapJoinsByJoinId) -> {
                                mapJoinsByJoinId.forEach((joinId, joinValue) -> setFieldValue(resultObject, rootFieldName, joinValue));
                            }));
                } else {
                    mapJoinsByJoinRootId.forEach((rootId, mapJoinsByRootFieldName) ->
                            mapJoinsByRootFieldName.forEach((rootFieldName, mapJoinsByJoinId) -> {
                                Map.Entry<String, Map<String, BaseEntity>> resultEntry = joins.get(entityId).get(resultObject.getClass())
                                        .get(entityId).entrySet().stream()
                                        .filter(entry -> entry.getValue().get(rootId) != null &&
                                                entry.getValue().get(rootId).getClass().equals(joinRootClass)).findFirst()
                                        .orElseThrow(() -> new IllegalArgumentException("Ошибка подготовки результата запроса"));

                                String resultFieldName = resultEntry.getKey();
                                Object objectToSetJoin = joins.get(entityId).get(resultObject.getClass()).get(entityId)
                                        .get(resultFieldName).get(rootId);
                                mapJoinsByJoinId.forEach((joinId, joinValue) -> setFieldValue(objectToSetJoin, rootFieldName, joinValue));
                            }));
                }
            });
        });

        return new ArrayList<E>(result.values());
    }

    private void getTupleJoin(Tuple tuple, TupleElement element, Class entityClass, E resultObject, Object elementValue,
                              Map<String, Map<Class, Map<String, Map<String, Map<String, BaseEntity>>>>> joins,
                              Class<? extends AbstractPathImpl> castClass) {
        Class currentElementClass;
        if (castClass.equals(SingularAttributePath.class)) {
            currentElementClass = (castClass.cast(element)).getAttribute().getJavaMember().getDeclaringClass();
        } else if (castClass.equals(SingularAttributeJoin.class)) {
            currentElementClass = castClass.cast(element).getJavaType();
        } else {
            throw new IllegalArgumentException("Ошибка во время получения результата");
        }
        String parameterName = (castClass.cast(element)).getAttribute().getName();
        if (currentElementClass.equals(entityClass)) {
            setFieldValue(resultObject, parameterName, elementValue);
        } else {
            AtomicReference<String> joinId = new AtomicReference<>();
            AtomicReference<String> joinRootId = new AtomicReference<>();
            tuple.getElements().stream().filter(el -> filterElementsToId(el, currentElementClass))
                    .forEach(idElement -> joinId.set(String.valueOf(tuple.get(tuple.getElements().indexOf(idElement)))));
            String rootFieldName = getRootFieldName(element);
            PathSource pathSource = (castClass.cast(element)).getPathSource();
            Class rootClass;
            if (pathSource.getClass().equals(ListAttributeJoin.class)) {
                if (castClass.equals(SingularAttributePath.class)) {
                    rootClass = (((ListAttributeJoin) (castClass.cast(element)).getPathSource())).getPathSource().getJavaType();
                } else {
                    rootClass = (((ListAttributeJoin) (castClass.cast(element)).getPathSource())).getAttribute().getElementType().getJavaType();
                }
                tuple.getElements().stream().filter(el -> filterElementsToId(el, rootClass))
                        .forEach(idElement -> joinRootId.set(String.valueOf(tuple.get(tuple.getElements().indexOf(idElement)))));
            } else if (pathSource.getClass().equals(SingularAttributeJoin.class)) {
                rootClass = (((SingularAttributeJoin) (castClass.cast(element)).getPathSource())).getPathSource().getJavaType();
                tuple.getElements().stream().filter(el -> filterElementsToId(el, rootClass))
                        .forEach(idElement -> joinRootId.set(String.valueOf(tuple.get(tuple.getElements().indexOf(idElement)))));
            } else if (pathSource.getClass().equals(RootImpl.class)) {
                rootClass = ((RootImpl) pathSource).getEntityType().getJavaType();
                tuple.getElements().stream().filter(el -> filterElementsToId(el, rootClass)).forEach(idElement -> {
                    joinRootId.set(String.valueOf(tuple.get(tuple.getElements().indexOf(idElement))));
                });
            } else {
                throw new IllegalArgumentException();
            }

            if (element.getClass().equals(SingularAttributeJoin.class)) {
                joins.computeIfAbsent(resultObject.getId(), (k) -> new HashMap<>())
                        .computeIfAbsent(rootClass, (k) -> new HashMap<>())
                        .computeIfAbsent(joinRootId.get(), (k) -> new HashMap<>())
                        .computeIfAbsent(rootFieldName, (k) -> new HashMap<>())
                        .putIfAbsent(joinId.get(), (BaseEntity) elementValue);
            } else {
                setFieldValue(joins.computeIfAbsent(resultObject.getId(), (k) -> new HashMap<>())
                        .computeIfAbsent(rootClass, (k) -> new HashMap<>())
                        .computeIfAbsent(joinRootId.get(), (k) -> new HashMap<>())
                        .computeIfAbsent(rootFieldName, (k) -> new HashMap<>())
                        .computeIfAbsent(joinId.get(),
                                (k) -> (BaseEntity) createClassInstance(currentElementClass)), parameterName, elementValue);
            }
        }
    }

    private List<Selection> getSelections(Condition condition, Root parentRoot) {
        E entity = (E) createClassInstance(getEntityClass());
        Map<String, List<String>> joinFields = new LinkedHashMap<>();

        List<String> maskFields = new ArrayList<>(condition.getMaskFields());
        List<String> parentFields =
                maskFields.stream().filter(field -> entity.getBaseFields().contains(field)).collect(Collectors.toList());

        maskFields.stream().filter(field -> !entity.getBaseFields().contains(field)).forEach(field -> {
            String[] fieldSplit = field.split("\\.");
            if (fieldSplit.length > 1) {
                if (joinFields.keySet().stream().anyMatch(joinName -> joinName.startsWith(fieldSplit[0]))
                        && condition.getMaskFields().contains(fieldSplit[0])) {
                    BaseEntity joinInstance = createJoinInstance(fieldSplit[0], entity.getClass());
                    joinFields.computeIfAbsent(fieldSplit[0], (k) -> new ArrayList<>()).addAll(joinInstance.getBaseFields());
                }
                List<String> joinFieldName = new ArrayList<>(Arrays.asList(fieldSplit).subList(0, fieldSplit.length - 1));
                String fieldName = String.join(".", joinFieldName);
                joinFields.computeIfAbsent(fieldName, (k) -> new ArrayList<>()).add(fieldSplit[fieldSplit.length - 1]);
            } else {
                joinFields.computeIfAbsent(field, (k) -> new ArrayList<>());
            }
        });

        return createSelections(parentFields, joinFields, parentRoot, entity);
    }

    private List<Selection> createSelections(List<String> parentFields, Map<String, List<String>> joins, From parentRoot, E entity) {
        List<Selection> result = new ArrayList<>();

        if (CollectionUtils.isEmpty(parentFields) && CollectionUtils.isEmpty(joins)) {
            result.add(parentRoot);
            return result;
        } else if (CollectionUtils.isEmpty(parentFields) && !CollectionUtils.isEmpty(joins)) {
            addSelections(result, parentRoot, entity.getBaseFields());
            joins.forEach((joinName, fields) -> parseJoin(joinName, fields, parentRoot, result, entity));
        } else if (!CollectionUtils.isEmpty(parentFields) && CollectionUtils.isEmpty(joins)) {
            addSelections(result, parentRoot, parentFields);
        } else if (!CollectionUtils.isEmpty(parentFields) && !CollectionUtils.isEmpty(joins)) {
            addSelections(result, parentRoot, parentFields);
            joins.forEach((joinName, fields) -> parseJoin(joinName, fields, parentRoot, result, entity));
        }
        return result;
    }

    private void parseJoin(String joinName, List<String> fields, From parentRoot, List<Selection> result, E entity) {
        String[] fieldSplit = joinName.split("\\.");
        if (fieldSplit.length > 1) {
            Join<E, ? extends BaseEntity> join = (Join<E, ? extends BaseEntity>) parentRoot.getJoins().stream()
                    .filter(j -> ((ListAttributeJoin) j).getAttribute().getName().equals(fieldSplit[0])).findFirst()
                    .orElseGet(() -> parentRoot.join(fieldSplit[0], JoinType.LEFT));
            String[] newFieldPath = Arrays.copyOfRange(fieldSplit, 1, fieldSplit.length);
            addSelections(result, getDeepSelections(newFieldPath, join), fields);
        } else if (!CollectionUtils.isEmpty(entity.getJoinFields()) && entity.getJoinFields().contains(joinName)) {
            Join<E, ? extends BaseEntity> join = (Join<E, ? extends BaseEntity>) parentRoot.getJoins().stream()
                    .filter(j -> ((ListAttributeJoin) j).getAttribute().getName().equals(joinName)).findFirst()
                    .orElseGet(() -> parentRoot.join(joinName, JoinType.LEFT));

            addSelections(result, join, fields);
        } else {
            throw new IllegalArgumentException("Неверно указано название поля: " + joinName);
        }
    }

    private Join getDeepSelections(String[] fieldPath, From from) {
        Join join;
        if (fieldPath.length > 1) {
            Join parentJoin = from.join(fieldPath[0], JoinType.LEFT);
            List<String> list = new ArrayList<>(Arrays.asList(fieldPath).subList(1, fieldPath.length));
            join = getDeepSelections(list.toArray(new String[0]), parentJoin);
        } else {
            join = from.join(fieldPath[0], JoinType.LEFT);
        }
        return join;
    }

    private void addSelections(List<Selection> selections, From from, List<String> fields) {
        if (CollectionUtils.isEmpty(fields)) {
            selections.add(from);
        } else {
            selections.add(from.get("id"));
            selections.addAll(fields.stream().filter(fieldName -> !fieldName.equals("id")).map(from::get).collect(Collectors.toList()));
        }
    }

    private BaseEntity createJoinInstance(String field, Class<? extends BaseEntity> entityClass) {
        BaseEntity joinInstance;
        try {
            Field joinField = entityClass.getDeclaredField(field);
            Class joinFieldClass = joinField.getType();
            if (joinFieldClass.equals(List.class) || joinFieldClass.getSuperclass().equals(Collection.class)) {
                joinInstance =
                        (BaseEntity) createClassInstance(((Class) ((ParameterizedType) joinField.getGenericType()).getActualTypeArguments()[0]));
            } else {
                joinInstance = (BaseEntity) createClassInstance(joinFieldClass);
            }
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
        return joinInstance;
    }

    private void setFieldValue(Object object, String parameterName, Object value) {
        if (value == null) {
            return;
        }
        try {
            Field field = object.getClass().getDeclaredField(parameterName);
            field.setAccessible(true);
            if (field.getType().equals(List.class)) {
                if (field.get(object) == null) {
                    field.set(object, Collections.singletonList(value));
                } else {
                    List listField = ((ArrayList) field.get(object));
                    if (!listField.contains(value)) {
                        listField.add(value);
                    }
                }
            } else {
                field.set(object, value);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException("Ошибка установки значения" + value + " для поля" + parameterName, e);
        }
    }

    private Object createClassInstance(Class objectClass) {
        try {
            return objectClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Ошибка создания экземпляра класса" + objectClass.getSimpleName(), e);
        }
    }

    private boolean filterElementsToId(TupleElement element, Class currentElementClass) {
        Class elClass = element.getClass();
        if (elClass.equals(SingularAttributePath.class)) {
            return ((SingularAttributePath) element).getAttribute().getName().equals("id")
                    && currentElementClass.equals(((SingularAttributePath) element).getAttribute().getJavaMember().getDeclaringClass());
        } else {
            return elClass.equals(ListAttributeJoin.class) && ((ListAttributeJoin) element).getAttribute().getName().equals("id")
                    && currentElementClass.equals(((ListAttributeJoin) element).getAttribute().getJavaMember().getDeclaringClass());
        }
    }

    private Object getFieldValue(Object obj) {
        try {
            Field field = obj.getClass().getDeclaredField("id");
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalArgumentException("Не возможно получить значение поля" + "id" + " объекта" + obj.toString(), e);
        }
    }

    private String getRootFieldName(TupleElement element) {
        Class elClass = element.getClass();
        if (elClass.equals(SingularAttributePath.class)) {
            Object attributeJoin = ((SingularAttributePath<?>) element).getParentPath();
            if (attributeJoin.getClass().equals(ListAttributeJoin.class)) {
                return ((ListAttributeJoin) (((SingularAttributePath<?>) element).getParentPath())).getAttribute().getName();
            } else if (attributeJoin.getClass().equals(SingularAttributeJoin.class)) {
                return ((SingularAttributeJoin) ((SingularAttributePath<?>) element).getParentPath()).getAttribute().getName();
            } else {
                return null;
            }
        } else if (elClass.equals(ListAttributeJoin.class)) {
            return ((ListAttributeJoin) element).getAttribute().getName();
        } else if (elClass.equals(SingularAttributeJoin.class)) {
            return (((SingularAttributeJoin) element)).getAttribute().getName();
        } else {
            return null;
        }
    }

    public EntityTransaction openTransaction(EntityManager entityManager) {
        EntityTransaction transaction = entityManager.getTransaction();
        if (!transaction.isActive()) {
            transaction.begin();
        }
        return transaction;
    }

    public void commitTransaction(EntityTransaction transaction) {
        if (transaction.isActive()) {
            transaction.commit();
        }
    }

    public void rollbackTransaction(EntityTransaction transaction) {
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }

    public void removeInOpenedTransaction(final Object entity, EntityManager entityManager) {
        if (entity != null) {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        }
    }

    public void save(BaseEntity entity) {
        EntityManager em = emf.createEntityManager();
        try {
            EntityTransaction transaction = openTransaction(em);
            em.persist(entity);
            commitTransaction(transaction);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void saveInOpenedTransaction(BaseEntity entity, EntityManager entityManager) {
        entityManager.persist(entity);
    }

    public void editInOpenedTransaction(BaseEntity entity, EntityManager entityManager) {
        entityManager.merge(entity);
    }
}