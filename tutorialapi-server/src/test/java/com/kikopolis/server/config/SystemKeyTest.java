package com.kikopolis.server.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SystemKeyTest {
    @Test
    public final void test_get_key() {
        assertEquals("8443", SystemKey.PORT.getDefaultValue(), "Failed asserting 'SystemKey.PORT.getDefaultValue()' returns '8443'");
        assertEquals("dev", SystemKey.MODE.getDefaultValue(), "Failed asserting 'SystemKey.MODE.getDefaultValue()' returns 'dev'");
    }
    @Test
    public final void test_get_key_default_values() {
        assertEquals("port", SystemKey.PORT.getKey(), "Failed asserting 'SystemKey.PORT.getKey' returns 'port'");
    }
}
