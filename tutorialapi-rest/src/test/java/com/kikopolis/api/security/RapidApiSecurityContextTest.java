package com.kikopolis.api.security;

import com.kikopolis.model.RapidApiPrincipal;
import com.kikopolis.model.Subscription;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RapidApiSecurityContextTest {
    @Test
    public void test_constructor() {
        RapidApiPrincipal principal = mock(RapidApiPrincipal.class);
        SecurityContext rapidApiSecurityContext = new RapidApiSecurityContext(principal);
        assertEquals(principal, rapidApiSecurityContext.getUserPrincipal());
        assertEquals("RapidAPI", rapidApiSecurityContext.getAuthenticationScheme());
    }
    
    @Test
    public void test_is_user_in_role() {
        RapidApiPrincipal principal = mock(RapidApiPrincipal.class);
        when(principal.getSubscription()).thenReturn(Subscription.from("basic").toString());
        SecurityContext rapidApiSecurityContext = new RapidApiSecurityContext(principal);
        assertTrue(rapidApiSecurityContext.isUserInRole("basic"));
    }
}
