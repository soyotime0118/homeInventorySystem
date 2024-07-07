package me.mason.management.ports.out;

import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;

import java.util.Optional;

public interface ModifyInventoryStateOutput {

    //재고수량변경

    /**
     * 재고 저장
     * @param inventory 변경할 재고
     */
    void modifyInventory(Inventory inventory);

    Optional<Inventory> loadByInventoryId(InventoryId inventoryId);

    /**
     * 새로운 재고추가
     *
     * @param inventoryName
     * @param inventoryQuantity
     * @param location
     * @return 추가한 재고ID
     */
    InventoryId addInventory(String inventoryName, int inventoryQuantity, String location);
}
