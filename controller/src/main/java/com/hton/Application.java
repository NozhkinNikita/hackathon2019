package com.hton;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;

import java.net.URL;

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

    @Bean
    public ConfigurableServletWebServerFactory servletContainerCustomizer() {
        JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
        factory.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/unauthenticated"));
        factory.addServerCustomizers((JettyServerCustomizer) server -> {
//            // HTTP
//            ServerConnector connector = new ServerConnector(server);
//            connector.setPort(8080);

            // HTTPS
            SslContextFactory sslContextFactory = new SslContextFactory();
            URL urlKeystore = getClass().getClassLoader().getResource("keyStore/ssl-server.jks");
            sslContextFactory.setKeyStoreResource(Resource.newResource(urlKeystore));
            sslContextFactory.setKeyStoreProvider("SUN");
            sslContextFactory.setKeyStoreType("JKS");
            sslContextFactory.setKeyStorePassword("changeit");
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