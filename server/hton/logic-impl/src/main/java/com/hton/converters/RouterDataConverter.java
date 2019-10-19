package com.hton.converters;

import com.hton.domain.RouterData;
import com.hton.entities.RouterDataEntity;
import org.springframework.stereotype.Component;

@Component
public class RouterDataConverter extends Converter<RouterData, RouterDataEntity> {

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
        super.toDomainObject(entity, domain);
    }

    @Override
    public void toEntityObject(RouterData domain, RouterDataEntity entity) {
        super.toEntityObject(domain, entity);
    }
}
