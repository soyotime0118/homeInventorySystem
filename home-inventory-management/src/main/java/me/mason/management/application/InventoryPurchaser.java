package me.mason.management.application;

import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.in.PurchaseInventoryInput;
import me.mason.management.ports.out.ModifyInventoryStateOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryPurchaser implements PurchaseInventoryInput {

    private final ModifyInventoryStateOutput inventoryPersistentRepository;

    @Autowired
    public InventoryPurchaser(ModifyInventoryStateOutput inventoryPersistentRepository) {
        this.inventoryPersistentRepository = inventoryPersistentRepository;
    }

    @Override
    public void purchaseInventory(InventoryId inventoryId, Integer quantity) {
        Inventory inventory = inventoryPersistentRepository.loadByInventoryId(inventoryId).orElseThrow();
        inventory.plusStock(quantity);
        inventoryPersistentRepository.modifyInventory(inventory);
    }
}
