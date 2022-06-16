package com.kikopolis.api.resource;

import com.kikopolis.api.ApiApplication;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestResourceTest extends JerseyTest {
    @Override
    protected Application configure() {
        return new ApiApplication();
    }
    
    @Test
    public void test() {
        Response response = target("/test").request().get();
        Assertions.assertEquals(200, response.getStatus());
    }
}
