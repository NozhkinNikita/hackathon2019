package com.hton.api;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebMvcConfig implements WebMvcConfigurer {

    public static final String ROOT_PATH = "/api";

    public static final String USER_INFO_PATH = ROOT_PATH + "/user-info";

    public static final String ADMIN_PATH = ROOT_PATH + "/admin";
    public static final String SECURITY_PATH = ROOT_PATH + "/security";
    public static final String USER_PATH = ROOT_PATH + "/user";

    public static final String SECURITY_USERS_PATH = SECURITY_PATH + "/users";
    public static final String SECURITY_LOCATION_PATH = SECURITY_PATH + "/location";

    public static final String ADMIN_ROUTER_PATH = ADMIN_PATH + "/routers";
    public static final String ADMIN_LOCATION_PATH = ADMIN_PATH + "/location";

    public static final String USER_SCAN_PATH = USER_PATH + "/scans";
    public static final String USER_POINT_PATH = USER_PATH + "/points";
    public static final String USER_LOCATION_PATH = USER_PATH + "/locations";

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
                .allowCredentials(true)
        ;
    }
}
