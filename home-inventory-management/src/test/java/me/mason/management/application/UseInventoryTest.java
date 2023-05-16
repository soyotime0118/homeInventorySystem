package me.mason.management.application;

import me.mason.management.domain.Inventory;
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
        int expectedValue = useInventory.usingInventory(new Inventory.InventoryId(2L), 1);
        int actualValue = 9;

        Assertions.assertEquals(expectedValue, actualValue);
    }

    @DisplayName("재고수량이 부족한경우 예외발생")
    @Test
    void throwNotEnoughStockAfterUseOne() {
        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            useInventory.usingInventory(new Inventory.InventoryId(2L), 11);
        });
    }

    @DisplayName("재고수량이 부족한경우 예외발생")
    @Test
    void throwNotFindStockAfterUseOne() {
        Assertions.assertThrows(NotFindStockException.class, () -> {
            useInventory.usingInventory(new Inventory.InventoryId(3L), 1);
        });
    }

    @Test
    public void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        //멀티스레드 이용 ExecutorService : 비동기를 단순하게 처리할 수 있또록 해주는 java api
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        //다른 스레드에서 수행이 완료될 때 까지 대기할 수 있도록 도와주는 API - 요청이 끝날때 까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                        try {
                            useInventory.usingInventory(new Inventory.InventoryId(2L), 1);
                        } finally {
                            latch.countDown();
                        }
                    }
            );
        }

        latch.await();

        Inventory inventory = inventoryPersistentRepository.loadByInventoryId(new Inventory.InventoryId(2L))
                .orElseThrow();

        //100 - (1*100) = 0
        Assertions.assertEquals(inventory.getQuantity(), 0);

    }

}