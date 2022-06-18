package com.kikopolis.api.security.header;

public enum CorsHeader {
    ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
    ACCESS_CONTROL_ALLOW_METHODS("Access-Control-Allow-Methods");
    
    private final String header;
    
    CorsHeader(String header) {
        this.header = header;
    }
    
    public String getHeader() {
        return header;
    }
}
