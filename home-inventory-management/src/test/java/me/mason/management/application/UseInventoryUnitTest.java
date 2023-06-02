package me.mason.management.application;

import me.mason.management.adapter.out.persistent.fake.FakeInventoryRepository;
import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.domain.NotEnoughStockException;
import me.mason.management.ports.in.UseInventoryInput;
import me.mason.management.ports.out.ModifyInventoryStateOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class UseInventoryUnitTest {

    UseInventoryInput useInventory;

    ModifyInventoryStateOutput modifyInventoryStateOutput;


    @BeforeEach
    void setUp() {
        Map<InventoryId, Inventory> idInventoryMap = new HashMap<>();
        idInventoryMap.put(InventoryId.of(1L), Inventory.withId(InventoryId.of(1L), "1번상품", 100));
        idInventoryMap.put(InventoryId.of(2L), Inventory.withId(InventoryId.of(2L), "2번상품", 100));
        modifyInventoryStateOutput = new FakeInventoryRepository(idInventoryMap);
        useInventory = new UseInventory(modifyInventoryStateOutput);
    }


    @DisplayName("재고 사용시 차감 테스트")
    @Test
    void useInventoryQuantity() {
        useInventory.usingInventory(InventoryId.of(1L), 1);

        Inventory inventory = modifyInventoryStateOutput.loadByInventoryId(InventoryId.of(1L))
                .orElseThrow();
        int actualValue = inventory.getQuantity();
        Assertions.assertEquals(99, actualValue);
    }
    @DisplayName("재고 사용시 재고부족한경우")
    @Test
    void useInventoryQuantity1() {
        Assertions.assertThrowsExactly(NotEnoughStockException.class,() -> {
            useInventory.usingInventory(InventoryId.of(1L), 101);
        });
    }
}
