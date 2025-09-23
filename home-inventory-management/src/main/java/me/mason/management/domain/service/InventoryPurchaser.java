package me.mason.management.domain.service;

import me.mason.management.infrastructure.adapter.in.web.command.PurchaseNewInventoryCommand;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.application.ports.in.PurchaseInventoryInput;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
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
    public void purchaseInventory(InventoryId inventoryId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must not be less than 0");
        }
        Inventory inventory = inventoryPersistentRepository.loadByInventoryId(inventoryId).orElseThrow();
        inventory.plusStock(quantity);
        inventoryPersistentRepository.modifyInventory(inventory);
    }

    @Override
    public InventoryId purchaseNewInventory(PurchaseNewInventoryCommand command) {
        Inventory newInventory = Inventory.builder()
                .itemName(command.getName())
                .quantity(Inventory.Quantity.of(command.getQuantity()))
                .locationId((long) command.getLocationId())
                .brand(command.getBrandName())
                .purchaseLocation(command.getPurchaseLocation())
                .lastPurchase(java.time.LocalDateTime.now())
                .build();
        Inventory saved = inventoryPersistentRepository.addInventory(newInventory);
        return saved.getInventoryId();
    }

}
