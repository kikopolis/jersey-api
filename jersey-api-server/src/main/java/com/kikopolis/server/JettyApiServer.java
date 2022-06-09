package com.kikopolis.server;

import com.kikopolis.api.ApiApplication;
import com.kikopolis.server.config.ConfigKey;
import com.kikopolis.server.config.SystemKey;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class JettyApiServer {
    private static final Logger logger = LoggerFactory.getLogger(JettyApiServer.class);
    
    public static void main(String[] args) throws Exception {
        int port = getServerPort();
        String mode = getServerMode();
        // Configuration file url
        String configUrl = String.format("https://raw.githubusercontent.com/kikopolis/jersey-api/master/system-%s.properties", mode);
        Config config;
        try {
            config = ConfigFactory.parseURL(new URL(configUrl));
            logger.info(config.getString(ConfigKey.API_PATTERN.getKey()));
        } catch (MalformedURLException e) {
            logger.error("Malformed url {}", configUrl);
            throw e;
        }
        Server server = createJettyServer(port, config);
        // Start the server or log the error thrown.
        try {
            logger.info("Starting server on port: {}", port);
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("Failed to start server on port: {}", port);
            throw e;
        }
    }
    
    private static Server createJettyServer(int port, Config config) throws IOException {
        Server server = new Server();
        // Create a new https configuration and enable secure scheme.
        HttpConfiguration httpsConfiguration = new HttpConfiguration();
        httpsConfiguration.setSecureScheme(HttpScheme.HTTPS.asString());
        httpsConfiguration.setSecurePort(port);
        httpsConfiguration.addCustomizer(new SecureRequestCustomizer());
        // Disable some headers for security reasons.
        httpsConfiguration.setSendServerVersion(false);
        httpsConfiguration.setSendDateHeader(false);
        // Create a new https connection factory using the httpsConfiguration.
        HttpConnectionFactory httpsConnectionFactory = new HttpConnectionFactory(httpsConfiguration);
        // Create a secure connection
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        // configure keystore and set ssl context to trust all
        sslContextFactory.setKeyStorePath(config.getString(ConfigKey.SERVER_KEYSTORE_FILE.getKey()));
        sslContextFactory.setKeyStoreType(config.getString(ConfigKey.SERVER_KEYSTORE_TYPE.getKey()));
        sslContextFactory.setKeyStorePassword(config.getString(ConfigKey.SERVER_KEYSTORE_PASSWORD.getKey()));
        sslContextFactory.setKeyManagerPassword(config.getString(ConfigKey.SERVER_KEYSTORE_PASSWORD.getKey()));
        sslContextFactory.setTrustAll(true);
        SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString());
        // Create a new server connector with SSL and https connections.
        // Set port, name and add https connector to server.
        ServerConnector httpsConnector = new ServerConnector(server, sslConnectionFactory, httpsConnectionFactory);
        httpsConnector.setPort(port);
//        httpsConnector.setName("secure");
        server.addConnector(httpsConnector);
        // Server connections are restful, set the context handler without persistent sessions.
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        // Set the context path as a slash to make all requests go through this context handler.
        servletContextHandler.setContextPath(ConfigKey.ROOT_CONTEXT.getKey());
        try {
            // Set the document root for context handler.
            servletContextHandler.setBaseResource(Resource.newResource(ConfigKey.SERVER_WEB_CONTENT.getKey()));
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        }
        // The path or uri for the context handler, a slash means all resources are server from the root url.
        servletContextHandler.addServlet(DefaultServlet.class, ConfigKey.ROOT_CONTEXT.getKey());
        server.setHandler(servletContextHandler);
        // Add the servlet for api uri paths.
        ServletHolder apiServletHolder = servletContextHandler.addServlet(ServletContainer.class, ConfigKey.API_PATTERN.getKey());
        // Set the api servlet place in servlet initialization order.
        apiServletHolder.setInitOrder(1);
        apiServletHolder.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, ApiApplication.class.getName());
        return server;
    }
    
    private static String getServerMode() {
        return Optional.ofNullable(System.getProperty(SystemKey.MODE.getKey()))
                .orElse(SystemKey.MODE.getDefaultValue());
    }
    
    private static int getServerPort() {
        return Integer.parseInt(Optional.ofNullable(System.getProperty(SystemKey.PORT.getKey()))
                .orElse(SystemKey.PORT.getDefaultValue()));
    }
}
