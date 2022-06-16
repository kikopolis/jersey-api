package com.kikopolis.server.config;

public enum SystemKey implements Key {
    PORT("8443"),
    MODE("dev");
    
    private final String defaultValue;
    
    SystemKey(String value) {
        defaultValue = value;
    }
    
    public String getDefaultValue() {
        return defaultValue;
    }
}
