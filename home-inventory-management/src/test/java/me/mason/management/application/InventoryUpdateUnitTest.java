package me.mason.management.application;

import me.mason.fake.FakeInventoryRepository;
import me.mason.management.application.ports.in.dto.UpdateInventoryCommand;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.model.InventoryFactory;
import me.mason.management.domain.service.InventoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryUpdateUnitTest {

    private InventoryManager inventoryManager;
    private ModifyInventoryStateOutput repo;

    @BeforeEach
    void setUp() {
        Map<InventoryId, Inventory> store = new HashMap<>();
        store.put(InventoryId.of(1L), InventoryFactory.withId(
                InventoryId.of(1L),
                "치약",
                2,
                1L,
                "LG",
                "쿠팡",
                LocalDateTime.of(2025, 1, 1, 0,0),
                null,
                LocalDateTime.of(2025, 12, 31, 0,0)
        ));
        repo = new FakeInventoryRepository(store);
        inventoryManager = new InventoryManager(repo);
    }

    @DisplayName("물품정보 수정 - 이름/브랜드/구매처/위치/구매일/유통기한")
    @Test
    void update_inventory_detail_with_dates() {
        // given
        UpdateInventoryCommand cmd = UpdateInventoryCommand.builder()
                .inventoryId(1L)
                .itemName("어금니치약")
                .brand("아모레")
                .purchaseLocation("마켓컬리")
                .locationId(2)
                .purchaseDate(LocalDateTime.of(2025, 2, 2, 12, 0))
                .expiryDate(LocalDateTime.of(2026, 1, 1, 0, 0))
                .build();

        // when
        Inventory updated = inventoryManager.updateInventory(cmd);

        // then
        assertThat(updated.getItemName()).isEqualTo("어금니치약");
        assertThat(updated.getBrand()).isEqualTo("아모레");
        assertThat(updated.getPurchaseLocation()).isEqualTo("마켓컬리");
        assertThat(updated.getLocationId()).isEqualTo(2L);
        assertThat(updated.getLastPurchase()).isEqualTo(LocalDateTime.of(2025, 2, 2, 12, 0));
        assertThat(updated.getExpiryDate()).isEqualTo(LocalDateTime.of(2026, 1, 1, 0, 0));
    }
}
