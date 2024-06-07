package me.mason.management.domain;

import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    void name() {
        Inventory inventory = Inventory.withoutId("name", 0,"", null);
    }
}