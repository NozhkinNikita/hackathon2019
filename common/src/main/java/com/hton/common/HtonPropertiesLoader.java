package com.hton.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.util.List;

//@Slf4j
public class HtonPropertiesLoader implements SpringApplicationRunListener {

    private static final YamlPropertySourceLoader YAML_LOADER = new YamlPropertySourceLoader();

    private static final String DATASOURCE_PROPERTIES_NAME = "hton.datasource.properties";
    private static final String PROPERTY_DATASOURCE_PROPERTIES_YAML_PATH = "hton.datasource.properties.yaml.path";


    public HtonPropertiesLoader(SpringApplication app, String[] args) {
    }

    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        loadProperties(environment, DATASOURCE_PROPERTIES_NAME, PROPERTY_DATASOURCE_PROPERTIES_YAML_PATH);
    }

    private void loadProperties(ConfigurableEnvironment environment, String propertiesName, String propertiesPathProperty) {
        String yamlPath = environment.getProperty(propertiesPathProperty);
        if (null == yamlPath) {
//            log.warn("{} is undefined. {} properties will not be loaded", propertiesPathProperty, propertiesName);
        } else {
            try {
                List<PropertySource<?>> propertySource = YAML_LOADER.load(
                        propertiesName,
                        new FileSystemResource(yamlPath));
                environment.getPropertySources().addLast(propertySource.get(0));
//                log.info("{} properties loaded from path {}", propertiesName, yamlPath);
            } catch (IOException | IllegalStateException e) {
//                log.warn("{} properties could not be loaded: {}. Exception message: {}",
//                        propertiesName, yamlPath, e.getMessage());
            }
        }
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }
}
