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
}
