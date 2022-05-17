package com.kikopolis.server;

import org.eclipse.jetty.server.HttpConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.jetty.http.HttpScheme.HTTPS;

public class JettyApiServer {
    private static final Logger logger = LoggerFactory.getLogger(JettyApiServer.class);
    
    public static void main(String[] args) {
        JettyApiServer.logger.info("HELLO WORLD");
        HttpConfiguration httpsConfiguration = new HttpConfiguration();
        httpsConfiguration.setSecureScheme(HTTPS.asString());
    }
}
