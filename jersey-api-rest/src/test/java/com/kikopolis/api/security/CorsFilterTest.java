package com.kikopolis.api.security;

import com.kikopolis.api.ApiApplication;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.test.JerseyTest;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.Test;

import java.util.logging.LogManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CorsFilterTest extends JerseyTest {
    static {
        // Disable logging from Java.util.logging in tests
        LogManager.getLogManager().reset();
    }
    
    @Override
    protected final Application configure() {
        return new ApiApplication();
    }
    
    @Test
    public final void corsHeaders() {
        WebTarget target = target("/test");
        Invocation.Builder request = target.request();
        Response response = request.get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertThat(response.getHeaders(), IsMapContaining.hasKey("Access-Control-Allow-Origin"));
        assertThat(response.getHeaders(), IsMapContaining.hasKey("Access-Control-Allow-Methods"));
    }
}
