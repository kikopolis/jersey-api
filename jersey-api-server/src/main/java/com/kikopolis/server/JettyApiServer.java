package com.kikopolis.server;

import org.eclipse.jetty.server.HttpConfiguration;

import static org.eclipse.jetty.http.HttpScheme.HTTPS;

public class JettyApiServer {
    public static void main(String[] args) {
        HttpConfiguration httpsConfiguration = new HttpConfiguration();
        httpsConfiguration.setSecureScheme(HTTPS.asString());
    }
}
