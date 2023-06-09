package me.mason.management.ports.out;

import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;

import java.util.Optional;

public interface ModifyInventoryStateOutput {

    //재고수량변경

    void modifyInventory(Inventory inventory);

    Optional<Inventory> loadByInventoryId(InventoryId inventoryId);

}
