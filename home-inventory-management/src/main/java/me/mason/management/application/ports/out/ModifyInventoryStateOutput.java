package me.mason.management.application.ports.out;

import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.model.Location.LocationId;

import java.util.List;
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

    /**
     * 주어진 인벤토리 ID를 사용하여 해당 인벤토리를 삭제합니다.
     *
     * @param inventoryId 삭제할 인벤토리의 고유 ID
     */
    void deleteInventory(InventoryId inventoryId);

    /**
     * 주어진 인벤토리 ID를 사용하여 해당 인벤토리가 존재하는지 확인합니다.
     *
     * @param inventoryId 확인할 인벤토리의 고유 ID
     * @return 인벤토리가 존재하면 true, 그렇지 않으면 false
     */
    boolean existsById(InventoryId inventoryId);

    // Query and specific operations
    List<Inventory> loadByContainingInventoryName(String inventoryName);

    List<Inventory> loadByLocationId(LocationId locationId);

    Optional<Inventory> updateInventoryLocation(InventoryId inventoryId, LocationId locationId);

    List<Inventory> findAll();
}
