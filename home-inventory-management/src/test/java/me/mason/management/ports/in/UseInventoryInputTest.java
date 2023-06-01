package me.mason.management.ports.in;

import me.mason.management.adapter.out.persistent.fake.FakeInventoryRepository;
import me.mason.management.application.UseInventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.domain.NotEnoughStockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UseInventoryInputTest {

    UseInventoryInput useInventoryInput;

    @BeforeEach
    void setUp() {
        FakeInventoryRepository fakeInventoryRepository = new FakeInventoryRepository();
        fakeInventoryRepository.addData(InventoryId.of(1L), "test", 10);
        fakeInventoryRepository.addData(InventoryId.of(2L), "test1", 10);
        useInventoryInput = new UseInventory(fakeInventoryRepository);
    }

    @DisplayName("재고수량이 여유있을때 1개를 사용하면 남은양을 반환한다")
    @Test
    void stockAvailable_whenUseOne_thenReturnRemaining() {
        int expectedValue = useInventoryInput.usingInventory(InventoryId.of(1L), 1);
        Assertions.assertEquals(expectedValue, 9);
    }

    @DisplayName("재고수량 충분하지 않으면 예외발생")
    @Test
    void stockNotAvailable_whenUseOne_thenThrowException() {
        Assertions.assertThrowsExactly(NotEnoughStockException.class, () -> {
            useInventoryInput.usingInventory(InventoryId.of(1L), 11);
        });
    }

    @DisplayName("사용하려는 재고가 존재하지 않으면 예외 발생")
    @Test
    void notFindStock_whenUseOne_thenThrowException() {
        Assertions.assertThrowsExactly(NotFindStockException.class, () -> {
            useInventoryInput.usingInventory(InventoryId.of(3L), 1);
        });
    }

}