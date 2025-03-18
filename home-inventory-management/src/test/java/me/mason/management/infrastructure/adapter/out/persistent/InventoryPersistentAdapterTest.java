package me.mason.management.infrastructure.adapter.out.persistent;

import me.mason.management.domain.model.Inventory.InventoryId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("develop")
class InventoryPersistentAdapterIntegrationTest {

    @Autowired
    private InventoryPersistentAdapter inventoryPersistentAdapter;

    private static final int INITIAL_QUANTITY = 1;
    private static final int INITIAL_LOCATION_ID = 1;
    private static final String TEST_ITEM_NAME = "Test Item";

    @Test
    void testSaveInventory() {
        InventoryId savedInventoryId = inventoryPersistentAdapter.addInventory(
                TEST_ITEM_NAME,
                INITIAL_QUANTITY,
                INITIAL_LOCATION_ID,
                "brandName",
                "purchaseLocation");
        Assertions.assertThat(savedInventoryId).isNotNull();
    }


}
