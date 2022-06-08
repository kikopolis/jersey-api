package com.kikopolis.server;

import com.kikopolis.api.ApiApplication;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.eclipse.jetty.http.HttpScheme.HTTPS;
import static org.eclipse.jetty.http.HttpVersion.HTTP_1_1;
import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

public class JettyApiServer {
    private static final Logger logger = LoggerFactory.getLogger(JettyApiServer.class);
    private static final Server server = new Server();
    /**
     * Default path to the https connection certificate keystore file.
     */
    private static final Path keyStorePath = Paths.get("jersey-api-server", "src", "main", "resources", "certs", "jerseyapi.p12");
    
    /**
     * Default content root for www accessible files.
     */
    private static final Path baseResourcePath = Paths.get("jersey-api-server", "src", "main", "resources", "www");
    /**
     * Default type of keystore that is used for the https connection certificate.
     */
    private static final String keyStoreType = "PKCS12";
    /**
     * Keystore file password.
     */
    public static final String password = "changeit";
    /**
     * Default port of the webserver.
     */
    public static final int PORT = 8443;
    
    public static void main(String[] args) {
        // Create a new https configuration and enable secure scheme.
        HttpConfiguration httpsConfiguration = new HttpConfiguration();
        httpsConfiguration.setSecureScheme(HTTPS.asString());
        httpsConfiguration.setSecurePort(PORT);
        httpsConfiguration.addCustomizer(new SecureRequestCustomizer());
        // Disable some headers for security reasons.
        httpsConfiguration.setSendServerVersion(false);
        httpsConfiguration.setSendDateHeader(false);
        // Create a new https connection factory using the httpsConfiguration.
        HttpConnectionFactory httpsConnectionFactory = new HttpConnectionFactory(httpsConfiguration);
        // Create a secure connection
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        // configure keystore and set ssl context to trust all
        sslContextFactory.setKeyStorePath(getKeyStorePath());
        sslContextFactory.setKeyStoreType(getKeyStoreType());
        sslContextFactory.setKeyStorePassword(password);
        sslContextFactory.setKeyManagerPassword(password);
        sslContextFactory.setTrustAll(true);
        SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(sslContextFactory, HTTP_1_1.asString());
        // Create a new server connector with SSL and https connections.
        // Set port, name and add https connector to server.
        ServerConnector httpsConnector = new ServerConnector(server, sslConnectionFactory, httpsConnectionFactory);
        httpsConnector.setPort(PORT);
        httpsConnector.setName("secure");
        server.addConnector(httpsConnector);
        // Server connections are restful, set the context handler without persistent sessions.
        ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);
        // Set the context path as a slash to make all requests go through this context handler.
        servletContextHandler.setContextPath("/");
        try {
            // Set the document root for context handler.
            servletContextHandler.setBaseResource(Resource.newResource(getResourcePath()));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        // The path or uri for the context handler, a slash means all requests go through this handler.
        servletContextHandler.addServlet(DefaultServlet.class, "/");
        server.setHandler(servletContextHandler);
        // Add the servlet for api uri paths.
        ServletHolder apiServletHolder = servletContextHandler.addServlet(ServletContainer.class, "/api/*");
        // Set the api servlet place in servlet initialization order.
        apiServletHolder.setInitOrder(1);
        apiServletHolder.setInitParameter("jakarta.ws.rs.Application", ApiApplication.class.getName());
        // Start the server or log the error thrown.
        try {
            logger.info("Starting Jetty API server on port " + PORT);
            server.start();
            server.join();
        } catch (Exception e) {
            logger.error("Failed to start Jetty Server on port " + PORT);
        }
    }
    
    private static String getResourcePath() {
        return baseResourcePath.toString();
    }
    
    private static String getKeyStorePath() {
        return keyStorePath.toString();
    }
    
    private static String getKeyStoreType() {
        return keyStoreType;
    }
}
