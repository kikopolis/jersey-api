package com.kikopolis.api.security.header;

public enum CorsHeaderValue {
    ACCESS_CONTROL_ALLOW_ORIGIN("*"),
    ACCESS_CONTROL_ALLOW_METHODS("GET, POST, DELETE, PUT, OPTIONS, HEAD, PATCH");
    private final String header;
    
    CorsHeaderValue(String header) {
        this.header = header;
    }
    
    public String getValue() {
        return header;
    }
}
