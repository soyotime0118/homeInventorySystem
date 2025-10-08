package me.mason.management.domain.service;

import lombok.extern.slf4j.Slf4j;
import me.mason.management.application.ports.in.PurchaseInventoryInput;
import me.mason.management.application.ports.in.UseInventoryInput;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.model.Inventory.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
class InventoryConcurrencyServiceIT {

    @Autowired
    UseInventoryInput useInventory;

    @Autowired
    PurchaseInventoryInput purchaseInventory;

    @Autowired
    ModifyInventoryStateOutput repository;

    @DisplayName("서비스 계층: 동시에 사용/구입 실행시 최종 수량 검증")
    @Test
    void concurrent_use_and_purchase_service() throws Exception {
        InventoryId id = InventoryId.of(1L);
        int useCalls = 50;      // -50
        int purchaseCalls = 20; // +20

        repository.addInventory(Inventory.builder().inventoryId(InventoryId.of(1L)).itemName("name").quantity(Quantity.of(100)).build());


        int total = useCalls + purchaseCalls;
        ExecutorService pool = Executors.newFixedThreadPool(100);
        CountDownLatch ready = new CountDownLatch(total);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(total);
        List<Throwable> errors = new ArrayList<>();

        for (int i = 0; i < useCalls; i++) {
            pool.submit(() -> {
                try {
                    ready.countDown();
                    log.info("using is ready");
                    start.await();
                    useInventory.usingInventory(id, Quantity.of(1));
                } catch (Throwable t) {
                    errors.add(t);
                } finally {
                    done.countDown();
                }
            });
        }
        for (int i = 0; i < purchaseCalls; i++) {
            pool.submit(() -> {
                try {
                    ready.countDown();
                    log.info("purchase is ready");
                    start.await();
                    purchaseInventory.purchaseInventory(id, Quantity.of(1));
                } catch (Throwable t) {
                    errors.add(t);
                } finally {
                    done.countDown();
                }
            });
        }

        ready.await();
        start.countDown();
        done.await();
        pool.shutdown();

        var item = repository.findAll();
        System.out.println(item);
//        assertThat(errors).isEmpty();

        // Then: 최종 수량 = initial - 50 + 20
        // Repository를 주입받아 직접 조회하거나, 전용 QueryPort/Service로 조회하여 assertThat(actual).isEqualTo(expected);
    }
}