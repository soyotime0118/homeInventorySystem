package me.mason.management.domain;

import me.mason.management.domain.model.Inventory;
import org.junit.jupiter.api.Test;

class InventoryTest {

    @Test
    void name() {
        Inventory inventory = Inventory.withoutId("name", 0, 0, null);
    }
}