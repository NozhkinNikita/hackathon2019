package com.hton.converters;

import com.hton.domain.Router;
import com.hton.entities.RouterEntity;
import org.springframework.stereotype.Component;

@Component
public class RouterConverter implements Converter<Router, RouterEntity> {
    @Override
    public Router toDomainObject(RouterEntity entity) {
        Router router = null;
        if (entity != null) {
            router = new Router();
            router.setId(entity.getId());
            router.setIp(entity.getIp());
            router.setSsid(entity.getSsid());
            router.setPwd(entity.getPwd());
            router.setAdminLogin(entity.getAdminLogin());
            router.setAdminPwd(entity.getAdminPwd());
        }
        return router;
    }

    @Override
    public RouterEntity toEntityObject(Router domain) {
        RouterEntity entity = null;
        if (domain != null) {
            entity = new RouterEntity();
            entity.setId(domain.getId());
            entity.setIp(domain.getIp());
            entity.setSsid(domain.getSsid());
            entity.setPwd(domain.getPwd());
            entity.setAdminLogin(domain.getAdminLogin());
            entity.setAdminPwd(domain.getAdminPwd());
        }
        return entity;
    }
}
