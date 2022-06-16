package com.kikopolis.api.resource;

import com.kikopolis.api.ApiApplication;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestResourceTest extends JerseyTest {
    @Override
    protected final Application configure() {
        return new ApiApplication();
    }
    
    @Test
    public final void test_resources_test() {
        WebTarget target = target("/test");
        Invocation.Builder request = target.request();
        Response response = request.get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
