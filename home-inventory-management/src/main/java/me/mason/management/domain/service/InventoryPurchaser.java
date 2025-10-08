package me.mason.management.domain.service;

import lombok.extern.slf4j.Slf4j;
import me.mason.management.application.ports.in.dto.PurchaseNewInventoryCommand;
import me.mason.management.domain.exception.NotFindStockException;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.application.ports.in.PurchaseInventoryInput;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.model.Inventory.Quantity;
import me.mason.management.domain.model.Location.LocationId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class InventoryPurchaser implements PurchaseInventoryInput {

    private final ModifyInventoryStateOutput inventoryPersistentRepository;

    @Autowired
    public InventoryPurchaser(ModifyInventoryStateOutput inventoryPersistentRepository) {
        this.inventoryPersistentRepository = inventoryPersistentRepository;
    }

    @Override
    @Transactional
    public Inventory purchaseInventory(InventoryId inventoryId, Quantity quantity) {
        Inventory inventory = inventoryPersistentRepository.loadByInventoryId(inventoryId)
                .orElseThrow(() -> new NotFindStockException(inventoryId));
        log.info("InventoryId {} is purchase, quantity : {}", inventoryId, quantity);
        inventory.plusStock(quantity);
        return inventoryPersistentRepository.updateInventory(inventory);
    }

    @Override
    @Transactional
    public Inventory purchaseNewInventory(PurchaseNewInventoryCommand command) {
        Inventory newInventory = Inventory.builder()
                .itemName(command.getName())
                .quantity(Inventory.Quantity.of(command.getQuantity()))
                .locationId(new LocationId((long) command.getLocationId()))
                .brand(command.getBrandName())
                .purchaseLocation(command.getPurchaseLocation())
                .lastPurchase(java.time.LocalDateTime.now())
                .build();
        return inventoryPersistentRepository.addInventory(newInventory);
    }

}
