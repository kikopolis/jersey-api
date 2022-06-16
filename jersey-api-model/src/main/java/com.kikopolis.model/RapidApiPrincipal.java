package com.kikopolis.model;

import java.security.Principal;
import java.util.Objects;

public class RapidApiPrincipal implements Principal {
    private final String user;
    private final String subscription;
    
    public RapidApiPrincipal(String user, String subscription) {
        this.user = user;
        this.subscription = subscription;
    }
    
    public final String getUser() {
        return user;
    }
    
    public final String getSubscription() {
        return subscription;
    }
    
    @Override
    public final String getName() {
        return null;
    }
    
    @Override
    public final String toString() {
        return "RapidApiPrincipal{" +
                "user='" + user + '\'' +
                ", subscription='" + subscription + '\'' +
                '}';
    }
    
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RapidApiPrincipal that = (RapidApiPrincipal) o;
        return user.equals(that.getUser()) && subscription.equals(that.getSubscription());
    }
    
    @Override
    public final int hashCode() {
        return Objects.hash(user, subscription);
    }
}
