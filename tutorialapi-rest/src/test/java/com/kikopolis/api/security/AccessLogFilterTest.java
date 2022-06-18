package com.kikopolis.api.security;

import com.kikopolis.model.RapidApiPrincipal;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.UriInfo;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccessLogFilterTest {
    @Test
    public final void test_filter_without_user() {
        String loggedLine = test(null, "GET", "/test");
        Assertions.assertEquals("? => GET /test", loggedLine);
    }
    
    @Test
    public final void test_filter_with_user() {
        String loggedLine = test(null, "GET", "/test");
        Assertions.assertEquals("? => GET /test", loggedLine);
    }
    
    @Test
    public final void test_filter_with_post_method() {
        String loggedLine = test(null, "POST", "/test");
        Assertions.assertEquals("? => POST /test", loggedLine);
    }
    
    private String test(String user, String method, String path) {
        UriInfo uriInfo = getUriInfo(path);
        ContainerRequestContext context = getContainerRequestContext(uriInfo, method);
        if (user != null) {
            addUserToContext(context, user);
        }
        List<String> logList = createAccessLogFilter(context);
        Assertions.assertEquals(1, logList.size());
        return logList.get(0);
    }
    
    private void addUserToContext(ContainerRequestContext context, String user) {
        RapidApiPrincipal principal = mock(RapidApiPrincipal.class);
        when(principal.getUser()).thenReturn(user);
        RapidApiSecurityContext securityContext = mock(RapidApiSecurityContext.class);
        when(securityContext.getUserPrincipal()).thenReturn(principal);
        when(context.getSecurityContext()).thenReturn(securityContext);
    }
    
    private List<String> createAccessLogFilter(ContainerRequestContext context) {
        List<String> logList = new ArrayList<>();
        AccessLogFilter accessLogFilter = new AccessLogFilter(logList::add);
        accessLogFilter.filter(context);
        return logList;
    }
    
    private ContainerRequestContext getContainerRequestContext(UriInfo uriInfo, String method) {
        ContainerRequestContext context = mock(ContainerRequestContext.class);
        when(context.getUriInfo()).thenReturn(uriInfo);
        when(context.getMethod()).thenReturn(method);
        return context;
    }
    
    private UriInfo getUriInfo(String path) {
        UriInfo uriInfo = mock(UriInfo.class);
        URI uri = URI.create(path);
        when(uriInfo.getAbsolutePath()).thenReturn(uri);
        return uriInfo;
    }
}
