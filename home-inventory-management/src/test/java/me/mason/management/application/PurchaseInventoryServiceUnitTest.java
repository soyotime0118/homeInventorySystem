package me.mason.management.application;

import me.mason.management.adapter.out.persistent.InventoryPersistentRepository;
import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.in.PurchaseInventoryInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PurchaseInventoryServiceUnitTest {

    PurchaseInventoryInput purchaseInventoryService;

    @Mock
    InventoryPersistentRepository inventoryPersistentRepository;

    @Mock
    Inventory inventory;

    @BeforeEach
    void setUp() {
        purchaseInventoryService = new InventoryPurchaser(inventoryPersistentRepository);
    }

    @DisplayName("물건을 구매처리하면, 물건의 재고가 구매수량만큼 증가한다")
    @Test
    void test() {

        InventoryId inventoryId = InventoryId.of(1L);
        givenInventoryWithId(inventoryId);

        //어떤 것을 테스트 할지는 개발자가 판단해야 한다. 디테일한 내용을 테스트하게되면 코드가 수정될때 테스트코드도 같이 수정될 여지가 있다.
        // 반면에 테스트가 추상적으로 될 수록 무의미한 테스트가 될 수 있다.
        //여기서는 물건구매시 해당물건의 수량이 증가하는것을 테스트 한다
        purchaseInventoryService.purchaseInventory(inventoryId, 10);

        then(inventory).should().plusStock(eq(10));

    }

    private void givenInventoryWithId(InventoryId inventoryId) {
        given(inventory.getInventoryId())
                .willReturn(inventoryId);
        given(inventoryPersistentRepository.loadByInventoryId(inventory.getInventoryId()))
                .willReturn(Optional.of(inventory));
    }

}