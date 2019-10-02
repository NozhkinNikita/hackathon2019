package com.hton;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.WebApplicationContext;

@SpringBootApplication
@ComponentScan(basePackages = {"com.hton.api", "com.hton.service",})
@EntityScan("com.hton.entity")
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Override
    protected WebApplicationContext run(SpringApplication application) {
        WebApplicationContext appContext = super.run(application);
        return appContext;
    }
}
