package me.mason.management.ports.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.mason.management.domain.Inventory.InventoryId;

public interface UseInventoryInput {

    /**
     * @return 재고 사용후 재고상태
     */
    int usingInventory(InventoryId inventoryId, Integer quantity);


}
