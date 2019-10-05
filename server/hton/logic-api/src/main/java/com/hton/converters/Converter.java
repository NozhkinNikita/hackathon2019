package com.hton.converters;

public interface Converter<D, E> {

    D toDomainObject(E entity);

    E toEntityObject(D domain);
}
