package com.kikopolis.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RapidApiPrincipalTest {
    private final String user = "user";
    private final String subscription = "basic";
    private final String proxySecret = "fxg4secret#231!@";
    
    @Test
    public final void test_get_user() {
        RapidApiPrincipal principal = getRapidApiPrincipal();
        assertEquals(user, principal.getUser());
    }
    
    @Test
    public final void test_get_subscription() {
        RapidApiPrincipal principal = getRapidApiPrincipal();
        assertEquals(subscription, principal.getSubscription());
    }
    
    @Test
    public final void test_get_proxy_secret() {
        RapidApiPrincipal principal = getRapidApiPrincipal();
        assertEquals(proxySecret, principal.getProxySecret());
    }
    
    @Test
    public final void test_get_name() {
        RapidApiPrincipal principal = getRapidApiPrincipal();
        assertEquals(user, principal.getName());
    }
    
    private RapidApiPrincipal getRapidApiPrincipal() {
        return new RapidApiPrincipal(user, subscription, proxySecret);
    }
}