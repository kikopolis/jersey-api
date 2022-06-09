package com.kikopolis.server.config;

import java.util.Locale;

public enum SystemKey {
    PORT("8443"),
    MODE("dev");
    
    private final String defaultValue;
    
    SystemKey(String value) {
        this.defaultValue = value;
    }
    
    public String getKey() {
        return name().toLowerCase(Locale.ENGLISH).toLowerCase().replaceAll("_", ".");
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }
}
