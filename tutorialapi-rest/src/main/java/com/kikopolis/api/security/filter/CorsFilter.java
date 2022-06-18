package com.kikopolis.api.security.filter;

import com.kikopolis.api.security.header.CorsHeader;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) {
        MultivaluedMap<String, Object> headers = containerResponseContext.getHeaders();
        headers.add(CorsHeader.ACCESS_CONTROL_ALLOW_ORIGIN.getHeader(), "*");
        headers.add(CorsHeader.ACCESS_CONTROL_ALLOW_METHODS.getHeader(), "GET, POST, DELETE, PUT, OPTIONS, HEAD, PATCH");
    }
}
