package com.kikopolis.api.resource;

import com.kikopolis.api.ApiApplication;
import com.kikopolis.api.security.header.CorsHeader;
import com.kikopolis.api.security.header.CorsHeaderValue;
import com.kikopolis.api.security.header.SecurityHeader;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import java.util.logging.LogManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloResourceIT extends JerseyTest {
    private final String user = "user";
    private final String proxySecret = "secret";
    private final String subscription = "basic";
    
    static {
        LogManager.getLogManager().reset();
    }
    
    @Override
    protected Application configure() {
        return new ApiApplication();
    }
    
    @Test
    public void test_hello_resource_with_no_security_headers() {
        WebTarget target = target("/test");
        Invocation.Builder request = target.request();
        Response response = request.get();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void test_hello_resource_with_no_user_header() {
        WebTarget target = target("/test");
        Invocation.Builder request = target.request();
        request.headers(getHeaders(subscription, proxySecret));
        Response response = request.get();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void test_hello_resource_with_no_subscription_header() {
        WebTarget target = target("/test");
        Invocation.Builder request = target.request();
        request.headers(getHeaders(user, proxySecret));
        Response response = request.get();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void test_hello_resource_with_no_proxy_secret_header() {
        WebTarget target = target("/test");
        Invocation.Builder request = target.request();
        request.headers(getHeaders(user, subscription));
        Response response = request.get();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void test_hello_resource() {
        WebTarget target = target("/test");
        Invocation.Builder request = target.request();
        request.headers(getHeaders(user, subscription, proxySecret));
        Response response = request.get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(
                CorsHeaderValue.ACCESS_CONTROL_ALLOW_ORIGIN.getValue(),
                response.getHeaderString(CorsHeader.ACCESS_CONTROL_ALLOW_ORIGIN.getHeader())
        );
        assertEquals(
                CorsHeaderValue.ACCESS_CONTROL_ALLOW_METHODS.getValue(),
                response.getHeaderString(CorsHeader.ACCESS_CONTROL_ALLOW_METHODS.getHeader())
        );
    }
    
    private MultivaluedMap<String, Object> getHeaders(String... requiredHeaders) {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        for (String s : requiredHeaders) {
            addHeader(s, headers);
        }
        return headers;
    }
    
    private void addHeader(String headerName, MultivaluedMap<String, Object> headers) {
        switch (headerName) {
            case user -> headers.add(SecurityHeader.X_RAPID_API_USER.getHeader(), user);
            case subscription -> headers.add(SecurityHeader.X_RAPID_API_SUBSCRIPTION.getHeader(), subscription);
            case proxySecret -> headers.add(SecurityHeader.X_RAPID_API_PROXY_SECRET.getHeader(), proxySecret);
        }
    }
}
