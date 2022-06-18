package com.kikopolis.api.security;

import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CorsFilterTest {
    @Test
    public final void test_filter() {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        ContainerResponseContext context = mock(ContainerResponseContext.class);
        when(context.getHeaders()).thenReturn(headers);
        new CorsFilter().filter(null, context);
        assertEquals(2, headers.size());
        assertTrue(headers.containsKey("Access-Control-Allow-Origin"));
        assertTrue(headers.containsKey("Access-Control-Allow-Methods"));
        assertEquals("[*]", headers.get("Access-Control-Allow-Origin").toString());
        assertEquals("[GET, POST, DELETE, PUT, OPTIONS, HEAD, PATCH]",
                headers.get("Access-Control-Allow-Methods").toString());
    }
}
