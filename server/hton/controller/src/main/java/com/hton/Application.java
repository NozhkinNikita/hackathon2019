package com.hton;

import com.hton.config.ServerConfigurator;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;

@SpringBootApplication(scanBasePackages = {
        "com.hton.api",
        "com.hton.config",
        "com.hton.config.jwt",
        "com.hton.converters",
        "com.hton.dao",
        "com.hton.service"
})
@EntityScan("com.hton.entities")
@EnableConfigurationProperties
@EnableScheduling
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private ServerConfigurator serverConfigurator;

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Override
    protected WebApplicationContext run(SpringApplication application) {
        WebApplicationContext appContext = super.run(application);
        return appContext;
    }

    @Bean
    public ConfigurableServletWebServerFactory servletContainerCustomizer() {
        JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
//        factory.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/unauthenticated"));
        factory.addServerCustomizers((JettyServerCustomizer) server -> {
//            // HTTP
//            ServerConnector connector = new ServerConnector(server);
//            connector.setPort(8080);

            // HTTPS
            SslContextFactory sslContextFactory = new SslContextFactory();
            URL urlKeystore = getClass().getClassLoader().getResource(serverConfigurator.getKeyStore());
            sslContextFactory.setKeyStoreResource(Resource.newResource(urlKeystore));
            sslContextFactory.setKeyStoreProvider(serverConfigurator.getKeyStoreProvider());
            sslContextFactory.setKeyStoreType(serverConfigurator.getKeyStoreType());
            sslContextFactory.setKeyStorePassword(serverConfigurator.getKeyPassword());
            sslContextFactory.setTrustAll(true);

            HttpConfiguration https = new HttpConfiguration();
            https.addCustomizer(new SecureRequestCustomizer());

            ServerConnector sslConnector = new ServerConnector(
                    server,
                    new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                    new HttpConnectionFactory(https));
            sslConnector.setPort(8443);

//            server.setConnectors(new Connector[] { connector, sslConnector });
            server.setConnectors(new Connector[]{sslConnector});

        });
        return factory;
    }
}