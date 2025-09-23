package me.mason.management.application;

import me.mason.fake.FakeInventoryRepository;
import me.mason.management.domain.model.InventoryFactory;
import me.mason.management.infrastructure.adapter.in.web.command.PurchaseNewInventoryCommand;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.application.ports.in.PurchaseInventoryInput;
import me.mason.management.domain.service.InventoryPurchaser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@DisplayName("재고 구입 테스트")
class InventoryPurchaserUnitTest {

    PurchaseInventoryInput purchaseInventoryInput;


    @DisplayName("재고구입시 구입한 재고를 검색할 수 있다.")
    @Test
    void purchaseNewInventory() {
        FakeInventoryRepository fakeInventoryRepository = new FakeInventoryRepository();
        purchaseInventoryInput = new InventoryPurchaser(fakeInventoryRepository);

        PurchaseNewInventoryCommand command = new PurchaseNewInventoryCommand("test", 1, 1);
        InventoryId inventoryId = purchaseInventoryInput.purchaseNewInventory(command);

        Inventory actual = fakeInventoryRepository.loadByInventoryId(inventoryId).orElseThrow();

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getItemName()).isEqualTo("test");
        Assertions.assertThat(actual.getInventoryId()).isNotNull();
    }

    @DisplayName("존재하는 재고를 구입하면 수량이증가한다")
    @Test
    void purchaseExistInventory() {
        Map<InventoryId, Inventory> inventoryMap = new HashMap<>();
        inventoryMap.put(InventoryId.of(1L), InventoryFactory.withId(InventoryId.of(1L), "test", 1, 1L, "", "", LocalDateTime.now(),null, null));
        FakeInventoryRepository fakeInventoryRepository = new FakeInventoryRepository(inventoryMap);
        purchaseInventoryInput = new InventoryPurchaser(fakeInventoryRepository);

        purchaseInventoryInput.purchaseInventory(InventoryId.of(1L), 1);

        Inventory actual = fakeInventoryRepository.loadByInventoryId(InventoryId.of(1L)).orElseThrow();

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getItemName()).isEqualTo("test");
        Assertions.assertThat(actual.getQuantity().value()).isEqualTo(2L);
        Assertions.assertThat(actual.getInventoryId()).isNotNull();

    }

    @DisplayName("구매수량이 0이면 IllegalArgumentException 발생")
    @Test
    void purchaseInventoryQuantityZero_throwException() {
        Map<InventoryId, Inventory> inventoryMap = new HashMap<>();
        inventoryMap.put(InventoryId.of(1L), InventoryFactory.withId(InventoryId.of(1L), "test", 1, 1L, "", "", LocalDateTime.now(),null, null));
        FakeInventoryRepository fakeInventoryRepository = new FakeInventoryRepository(inventoryMap);
        purchaseInventoryInput = new InventoryPurchaser(fakeInventoryRepository);

        Assertions.assertThatCode(() -> {
            purchaseInventoryInput.purchaseInventory(InventoryId.of(1L), 0);
        }).isInstanceOf(IllegalArgumentException.class);

//        Inventory actual = fakeInventoryRepository.loadByInventoryId(InventoryId.of(1L)).orElseThrow();
//
//        Assertions.assertThat(actual).isNotNull();
//        Assertions.assertThat(actual.getName()).isEqualTo("test");
//        Assertions.assertThat(actual.getQuantity()).isEqualTo(2);
//        Assertions.assertThat(actual.getInventoryId()).isNotNull();

    }
}