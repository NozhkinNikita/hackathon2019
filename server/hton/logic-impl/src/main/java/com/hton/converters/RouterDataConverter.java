package com.hton.converters;

import com.hton.domain.Router;
import com.hton.domain.RouterData;
import com.hton.entities.RouterDataEntity;
import com.hton.entities.RouterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouterDataConverter extends Converter<RouterData, RouterDataEntity> {

    @Autowired
    private Converter<Router, RouterEntity> routerConverter;

    @Override
    public Class<RouterData> getDomainClass() {
        return RouterData.class;
    }

    @Override
    public Class<RouterDataEntity> getEntityClass() {
        return RouterDataEntity.class;
    }

    @Override
    public void toDomainObject(RouterDataEntity entity, RouterData domain) {
        if (entity != null) {
            Router router = new Router();
            routerConverter.toDomainObject(entity.getOurRouter(), router);
            domain.setOurRouter(router);
            super.toDomainObject(entity, domain);
        }
    }

    @Override
    public void toEntityObject(RouterData domain, RouterDataEntity entity) {
        if (domain != null) {
            RouterEntity routerEntity = new RouterEntity();
            routerConverter.toEntityObject(domain.getOurRouter(), routerEntity);
            entity.setOurRouter(routerEntity);
            super.toEntityObject(domain, entity);
        }
    }
}
