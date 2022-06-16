package com.kikopolis.model;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SubscriptionTest {
    @Test
    public final void test_from() {
        assertEquals(Optional.of(Subscription.BASIC), Subscription.from("BASIC"));
        assertEquals(Optional.of(Subscription.BASIC), Subscription.from("basic"));
        assertNotEquals(Optional.of(Subscription.BASIC), Subscription.from("dasfgr"));
    }
}