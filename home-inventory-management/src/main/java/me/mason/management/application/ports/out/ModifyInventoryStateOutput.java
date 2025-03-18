package me.mason.management.application.ports.out;

import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;

import java.util.Optional;

public interface ModifyInventoryStateOutput {

    //재고수량변경

    /**
     * 재고 저장
     *
     * @param inventory 변경할 재고
     */
    void modifyInventory(Inventory inventory);

    Optional<Inventory> loadByInventoryId(InventoryId inventoryId);

    /**
     * 새로운 재고추가
     *
     * @return 추가한 재고
     */
    Inventory addInventory(Inventory inventory);

    /**
     * 재고 정보를 업데이트합니다.
     *
     * @param inventory 업데이트할 재고 객체
     * @return 업데이트된 재고 객체
     */
    Inventory updateInventory(Inventory inventory);
}
