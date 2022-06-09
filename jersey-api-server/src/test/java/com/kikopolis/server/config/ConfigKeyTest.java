package com.kikopolis.server.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigKeyTest {
    @Test
    public final void testGetKey() {
        Assertions.assertEquals("server.keystore.file", ConfigKey.SERVER_KEYSTORE_FILE.getKey(), "Failed asserting 'ConfigKey.SERVER_KEYSTORE_FILE.getKey' returns 'server.keystore.file'");
    }
}
