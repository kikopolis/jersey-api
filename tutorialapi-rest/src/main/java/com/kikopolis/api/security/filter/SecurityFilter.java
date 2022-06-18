package com.kikopolis.api.security.filter;

import com.kikopolis.api.security.RapidApiSecurityContext;
import com.kikopolis.api.security.header.SecurityHeader;
import com.kikopolis.model.RapidApiPrincipal;
import com.kikopolis.model.Subscription;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

import static com.kikopolis.api.security.header.SecurityHeader.*;

@Provider
public class SecurityFilter implements ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
    private final List<String> errors = new ArrayList<>();
    
    @Override
    public void filter(ContainerRequestContext context) throws NotAuthorizedException {
        Optional<String> proxySecret = getHeader(context, X_RAPID_API_PROXY_SECRET.getHeader());
        Optional<String> user = getHeader(context, X_RAPID_API_USER.getHeader());
        Optional<Subscription> subscription = getHeader(context, X_RAPID_API_SUBSCRIPTION.getHeader()).flatMap(Subscription::from);
        if (proxySecret.isEmpty()) {
            addToErrorList(X_RAPID_API_PROXY_SECRET);
        }
        if (user.isEmpty()) {
            addToErrorList(X_RAPID_API_USER);
        }
        if (subscription.isEmpty()) {
            addToErrorList(X_RAPID_API_SUBSCRIPTION);
        }
        if (!errors.isEmpty()) {
            throw new NotAuthorizedException(String.format("Missing security headers: %s", errors), Response.status(Response.Status.UNAUTHORIZED));
        }
        RapidApiPrincipal principal = new RapidApiPrincipal(proxySecret.get(), user.get(), subscription.get().toString());
        logger.info("User Principal: {}", principal);
        context.setSecurityContext(new RapidApiSecurityContext(principal));
    }
    
    private Optional<String> getHeader(ContainerRequestContext context, String header) {
        return Stream.of(context.getHeaders())
                .filter(Objects::nonNull)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .filter(entry -> entry.getKey().equalsIgnoreCase(header))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .findFirst();
    }
    
    private void addToErrorList(SecurityHeader header) {
        errors.add(String.format("Missing or invalid security header: %s", header));
    }
}
