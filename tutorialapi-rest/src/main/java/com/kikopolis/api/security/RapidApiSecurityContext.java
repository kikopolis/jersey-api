package com.kikopolis.api.security;

import com.kikopolis.model.RapidApiPrincipal;
import com.kikopolis.model.Subscription;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
import java.util.Objects;

public class RapidApiSecurityContext implements SecurityContext {
    private final String authenticationScheme = "RapidAPI";
    private final RapidApiPrincipal principal;
    
    public RapidApiSecurityContext(RapidApiPrincipal principal) {
        this.principal = principal;
    }
    
    @Override
    public Principal getUserPrincipal() {
        return principal;
    }
    
    @Override
    public boolean isUserInRole(String role) {
        return Objects.equals(principal.getSubscription(), Subscription.from(role).toString());
    }
    
    @Override
    public boolean isSecure() {
        return true;
    }
    
    @Override
    public String getAuthenticationScheme() {
        return authenticationScheme;
    }
}
