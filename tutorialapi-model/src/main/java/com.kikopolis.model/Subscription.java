package com.kikopolis.model;

import java.util.Optional;

public enum Subscription {
    BASIC,
    PRO,
    ULTRA,
    MEGA,
    CUSTOM;
    
    public static Optional<Subscription> from(String value) {
        if (value != null) {
            for (Subscription s : values()) {
                String name = s.name();
                if (name.equalsIgnoreCase(value)) {
                    return Optional.of(s);
                }
            }
        }
        return Optional.empty();
    }
}
