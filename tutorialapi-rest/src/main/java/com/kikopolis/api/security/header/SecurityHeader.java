package com.kikopolis.api.security.header;

public enum SecurityHeader {
    X_RAPID_API_PROXY_SECRET("X-RapidAPI-Proxy-Secret"),
    X_RAPID_API_USER("X-RapidAPI-User"),
    X_RAPID_API_SUBSCRIPTION("X-RapidAPI-Subscription");
    
    private final String header;
    
    SecurityHeader(String header) {
        this.header = header;
    }
    
    public String getHeader() {
        return header;
    }
}
