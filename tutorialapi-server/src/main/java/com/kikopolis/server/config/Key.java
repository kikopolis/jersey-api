package com.kikopolis.server.config;

import java.util.Locale;

public interface Key {
    String name();
    
    default String getKey() {
        return name().toLowerCase(Locale.ENGLISH).toLowerCase().replaceAll("_", ".");
    }
}
