package com.kikopolis.server.config;

import java.util.Locale;

public enum ConfigKey {
    SERVER_KEYSTORE_FILE,
    SERVER_KEYSTORE_TYPE,
    SERVER_KEYSTORE_PASSWORD,
    SERVER_WEB_CONTENT,
    ROOT_CONTEXT,
    API_PATTERN;
    
    public String getKey(){
        return name().toLowerCase(Locale.ENGLISH).toLowerCase().replaceAll("_", ".");
    }
}
