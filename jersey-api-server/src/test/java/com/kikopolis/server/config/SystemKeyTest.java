package com.kikopolis.server.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SystemKeyTest {
    @Test
    public final void testGetKey() {
        Assertions.assertEquals("8443", SystemKey.PORT.getDefaultValue(), "Failed asserting 'SystemKey.PORT.getDefaultValue()' returns '8443'");
        Assertions.assertEquals("dev", SystemKey.MODE.getDefaultValue(), "Failed asserting 'SystemKey.MODE.getDefaultValue()' returns 'dev'");
    }
    @Test
    public final void testGetKeyDefaultValues() {
        Assertions.assertEquals("port", SystemKey.PORT.getKey(), "Failed asserting 'SystemKey.PORT.getKey' returns 'port'");
    }
}
