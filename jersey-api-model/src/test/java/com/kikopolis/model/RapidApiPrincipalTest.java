package com.kikopolis.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RapidApiPrincipalTest {
    
    @Test
    public final void getUser() {
        RapidApiPrincipal rapidApiPrincipal = new RapidApiPrincipal("user", "basic");
        assertEquals("user", rapidApiPrincipal.getUser());
        
    }
    
    @Test
    public final void getSubscription() {
        RapidApiPrincipal rapidApiPrincipal = new RapidApiPrincipal("user", "basic");
        assertEquals("basic", rapidApiPrincipal.getSubscription());
    }
    
//    @Test
//    public final void getName() {
//    }
}