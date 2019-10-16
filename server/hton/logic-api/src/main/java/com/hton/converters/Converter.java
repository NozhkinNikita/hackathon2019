package com.hton.converters;

import com.hton.entities.BaseEntity;

import java.beans.PropertyDescriptor;
import java.util.Arrays;

public abstract class Converter<D, E extends BaseEntity> {
    public abstract Class<D> getDomainClass();

    public abstract Class<E> getEntityClass();

    public void toDomainObject(E entity, D domain) {
        try {
            convert(entity, domain);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void toEntityObject(D domain, E entity) {
        try {
            convert(domain, entity);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void convert(Object from, Object to) {
        Class fromClass = from.getClass().equals(getDomainClass()) ? getDomainClass() : getEntityClass();
        Class toClass = to.getClass().equals(getDomainClass()) ? getDomainClass() : getEntityClass();

        Arrays.stream(toClass.getDeclaredFields()).forEach(field -> {
            try {
                PropertyDescriptor pdFrom = new PropertyDescriptor(field.getName(), fromClass);
                PropertyDescriptor pdTo = new PropertyDescriptor(field.getName(), toClass);
                Object valueInToObject = pdTo.getReadMethod().invoke(to);
                if (valueInToObject == null) {
                    Object value = pdFrom.getReadMethod().invoke(from);
                    pdTo.getWriteMethod().invoke(to, value);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        });
    }
}
