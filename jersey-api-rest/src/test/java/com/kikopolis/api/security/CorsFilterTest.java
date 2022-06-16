package com.kikopolis.api.security;

import com.kikopolis.api.ApiApplication;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Assertions;
import org.hamcrest.collection.IsMapContaining;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

public class CorsFilterTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new ApiApplication();
    }
    
    @Test
    public void corsHeaders() {
        Response response = target("/test").request().get();
        Assertions.assertEquals(200, response.getStatus());
        assertThat(response.getHeaders(), IsMapContaining.hasKey("Access-Control-Allow-Origin"));
        assertThat(response.getHeaders(), IsMapContaining.hasKey("Access-Control-Allow-Methods"));
    }
}
