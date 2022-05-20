package com.kikopolis.server;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.eclipse.jetty.http.HttpScheme.HTTPS;
import static org.eclipse.jetty.http.HttpVersion.HTTP_1_1;

public class JettyApiServer {
    private static final Logger logger = LoggerFactory.getLogger(JettyApiServer.class);
    private static final Server server = new Server();
    private static final Path keyStorePath = Paths.get("jersey-api-server", "src", "main", "resources", "certs", "jerseyapi.p12");
    private static final String keyStoreType = "PKCS12";
    public static final String password = "changeit";
    public static final int PORT = 8443;
    
    public static void main(String[] args) {
        HttpConfiguration        httpsConfiguration     = JettyApiServer.getHttpConfiguration();
        HttpConnectionFactory    httpsConnectionFactory = new HttpConnectionFactory(httpsConfiguration);
        SslContextFactory.Server sslContextFactory      = JettyApiServer.getSslContextFactory();
        SslConnectionFactory     sslConnectionFactory   = new SslConnectionFactory(sslContextFactory, HTTP_1_1.asString());
        JettyApiServer.createServerConnector(httpsConnectionFactory, sslConnectionFactory);
        try {
            JettyApiServer.server.start();
            JettyApiServer.server.join();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start Jetty Server", e);
        }
    }
    
    private static void createServerConnector(HttpConnectionFactory httpsConnectionFactory, SslConnectionFactory sslConnectionFactory) {
        ServerConnector httpsConnector = new ServerConnector(JettyApiServer.server, sslConnectionFactory, httpsConnectionFactory);
        httpsConnector.setPort(JettyApiServer.PORT);
        httpsConnector.setName("secure");
        JettyApiServer.server.addConnector(httpsConnector);
    }
    
    private static SslContextFactory.Server getSslContextFactory() {
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(JettyApiServer.getKeyStorePath());
        sslContextFactory.setKeyStoreType(JettyApiServer.getKeyStoreType());
        sslContextFactory.setKeyStorePassword(JettyApiServer.password);
        sslContextFactory.setKeyManagerPassword(JettyApiServer.password);
        sslContextFactory.setTrustAll(true);
        return sslContextFactory;
    }
    
    private static HttpConfiguration getHttpConfiguration() {
        HttpConfiguration httpsConfiguration = new HttpConfiguration();
        httpsConfiguration.setSecureScheme(HTTPS.asString());
        httpsConfiguration.setSecurePort(JettyApiServer.PORT);
        httpsConfiguration.addCustomizer(new SecureRequestCustomizer());
        httpsConfiguration.setSendServerVersion(false);
        return httpsConfiguration;
    }
    
    private static String getKeyStorePath() {
        return JettyApiServer.keyStorePath.toString();
    }
    
    private static String getKeyStoreType() {
        return JettyApiServer.keyStoreType;
    }
}
