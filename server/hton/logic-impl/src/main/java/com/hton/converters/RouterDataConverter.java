package com.hton.converters;

import com.hton.domain.RouterData;
import com.hton.domain.User;
import com.hton.entities.RouterDataEntity;
import com.hton.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
        if (entity != null) {
            super.toDomainObject(entity, domain);
        }
    }

    @Override
    public void toEntityObject(RouterData domain, RouterDataEntity entity) {
        if (domain != null) {
            super.toEntityObject(domain, entity);
        }
    }
}
