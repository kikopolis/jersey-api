package com.kikopolis.api.security;

import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.kikopolis.api.security.SecurityHeader.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurityFilterTest {
    private final String user = "user";
    private final String subscription = "basic";
    private final String proxySecret = "fxg4secret#231!@";
    @Mock
    private ContainerRequestContext context;
    
    @BeforeEach
    void setUp() {
        context = mock(ContainerRequestContext.class);
    }
    
    @Test
    public final void test_filter() {
        when(context.getHeaders()).thenReturn(getHeaders(createExclusionList()));
        SecurityFilter filter = new SecurityFilter();
        assertDoesNotThrow(() -> filter.filter(context));
    }
    
    @Test
    public final void test_filter_throws_when_all_headers_empty() {
        SecurityFilter filter = new SecurityFilter();
        assertThrows(NotAuthorizedException.class, () -> filter.filter(context));
    }
    
    @ParameterizedTest
    @EnumSource(SecurityHeader.class)
    public final void test_filter_throws_when_single_header_empty(SecurityHeader header) {
        when(context.getHeaders()).thenReturn(getHeaders(createExclusionList(header.getHeader())));
        SecurityFilter filter = new SecurityFilter();
        assertThrows(NotAuthorizedException.class, () -> filter.filter(context));
    }
    
    private List<String> createExclusionList(String... exclusions) {
        return new ArrayList<>(Arrays.asList(exclusions));
    }
    
    private MultivaluedMap<String, String> getHeaders(Iterable<String> exclusions) {
        MultivaluedMap<String, String> headers = constructValidHeaders();
        for (String exclusion : exclusions) {
            headers.remove(exclusion);
        }
        return headers;
    }
    
    private MultivaluedMap<String, String> constructValidHeaders() {
        MultivaluedMap<String, String> map = new MultivaluedHashMap<>();
        map.add(X_RAPID_API_USER.getHeader(), user);
        map.add(X_RAPID_API_SUBSCRIPTION.getHeader(), subscription);
        map.add(X_RAPID_API_PROXY_SECRET.getHeader(), proxySecret);
        return map;
    }
}