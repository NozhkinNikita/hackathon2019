package com.hton.converters;

import com.hton.domain.Router;
import com.hton.entities.RouterEntity;
import org.springframework.stereotype.Component;

@Component
public class RouterConverter extends Converter<Router, RouterEntity> {
    @Override
    public Class<Router> getDomainClass() {
        return Router.class;
    }

    @Override
    public Class<RouterEntity> getEntityClass() {
        return RouterEntity.class;
    }
}
