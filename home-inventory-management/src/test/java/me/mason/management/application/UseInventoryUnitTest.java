package me.mason.management.application;

import me.mason.fake.FakeInventoryRepository;
import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.domain.NotEnoughStockException;
import me.mason.management.ports.in.NotFindStockException;
import me.mason.management.ports.in.UseInventoryInput;
import me.mason.management.ports.out.ModifyInventoryStateOutput;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class UseInventoryUnitTest {

    UseInventoryInput useInventory;

    ModifyInventoryStateOutput modifyInventoryStateOutput;


    @BeforeEach
    void setUp() {
        Map<InventoryId, Inventory> idInventoryMap = new HashMap<>();
        idInventoryMap.put(InventoryId.of(1L), Inventory.withId(InventoryId.of(1L), "1번상품", 100, "", LocalDateTime.now()));
        idInventoryMap.put(InventoryId.of(2L), Inventory.withId(InventoryId.of(2L), "2번상품", 100, "", LocalDateTime.now()));
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

    @DisplayName("재고 사용시 예외")
    @Nested
    class ExceptionCase {
        @DisplayName("재고사용시 수량을 초과해서 사용하는경우")
        @Test
        void useInventoryQuantity_overQuantity() {
            Assertions.assertThrowsExactly(NotEnoughStockException.class, () -> {
                useInventory.usingInventory(InventoryId.of(1L), 101);
            });
        }

        @DisplayName("사용하려는 재고수량이 0 인경우")
        @Test
        void useInventoryQuantity_useQuantity_0() {
            Assertions.assertThrowsExactly(IllegalArgumentException.class, () -> {
                useInventory.usingInventory(InventoryId.of(1L), 0);
            });
        }

        @DisplayName("사용하려는 재고가 존재하지 않으면 예외 발생")
        @Test
        void notFindStock() {
            Assertions.assertThrowsExactly(NotFindStockException.class, () -> {
                useInventory.usingInventory(InventoryId.of(3L), 1);
            });
        }

    }

}
