package me.mason.management.application;

import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.domain.NotEnoughStockException;
import me.mason.management.ports.in.NotFindStockException;
import me.mason.management.ports.in.UseInventoryInput;
import me.mason.management.ports.out.ModifyInventoryStateOutput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//todo sql 초기화코드
@SqlGroup({
        @Sql(statements = "insert into inventory(id, name, quantity) values (2, '컵라면1', 100);"
                , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(statements = "truncate table inventory"
                , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)})
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UseInventoryTest {

    @Autowired
    UseInventoryInput useInventory;

    @Autowired
    ModifyInventoryStateOutput inventoryPersistentRepository;

    @DisplayName("특정 재고1개사용 후 반환갯수확인")
    @Test
    void checkRemainingStockAfterUseOne() {
        int expectedValue = useInventory.usingInventory(InventoryId.of(2L), 1);
        int actualValue = 9;

        Assertions.assertEquals(expectedValue, actualValue);
    }

    @DisplayName("재고수량이 부족한경우 예외발생")
    @Test
    void throwNotEnoughStockAfterUseOne() {
        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            useInventory.usingInventory(InventoryId.of(2L), 11);
        });
    }

    @DisplayName("재고수량이 부족한경우 예외발생")
    @Test
    void throwNotFindStockAfterUseOne() {
        Assertions.assertThrows(NotFindStockException.class, () -> {
            useInventory.usingInventory(InventoryId.of(3L), 1);
        });
    }


}