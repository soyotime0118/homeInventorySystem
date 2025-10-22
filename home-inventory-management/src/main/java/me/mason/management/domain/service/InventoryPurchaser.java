package me.mason.management.domain.service;

import lombok.extern.slf4j.Slf4j;
import me.mason.management.application.ports.in.dto.PurchaseNewInventoryCommand;
import me.mason.management.application.ports.out.EventPublisher;
import me.mason.management.domain.event.InventoryPurchasedEvent;
import me.mason.management.domain.event.InventoryStockAddedEvent;
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

import java.time.LocalDateTime;


@Slf4j
@Service
public class InventoryPurchaser implements PurchaseInventoryInput {

    private final ModifyInventoryStateOutput inventoryPersistentRepository;
    private final EventPublisher eventPublisher;

    @Autowired
    public InventoryPurchaser(
        ModifyInventoryStateOutput inventoryPersistentRepository,
        EventPublisher eventPublisher
    ) {
        this.inventoryPersistentRepository = inventoryPersistentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Inventory purchaseInventory(InventoryId inventoryId, Quantity quantity) {
        Inventory inventory = inventoryPersistentRepository.loadByInventoryId(inventoryId)
                .orElseThrow(() -> new NotFindStockException(inventoryId));
        log.info("InventoryId {} is purchase, quantity : {}", inventoryId, quantity);

        inventory.plusStock(quantity);
        Inventory updatedInventory = inventoryPersistentRepository.updateInventory(inventory);

        // 재고 수량 추가 이벤트 발행
        InventoryStockAddedEvent event = InventoryStockAddedEvent.builder()
            .inventoryId(updatedInventory.getInventoryId().getValue())
            .itemName(updatedInventory.getItemName())
            .addedQuantity(quantity.value())
            .totalQuantity(updatedInventory.getQuantity().value())
            .occurredOn(LocalDateTime.now())
            .build();

        eventPublisher.publish(event);
        log.info("Published InventoryStockAddedEvent for inventoryId: {}", inventoryId);

        return updatedInventory;
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

        Inventory savedInventory = inventoryPersistentRepository.addInventory(newInventory);

        // 신규 재고 구매 이벤트 발행
        InventoryPurchasedEvent event = InventoryPurchasedEvent.builder()
            .inventoryId(savedInventory.getInventoryId().getValue())
            .itemName(savedInventory.getItemName())
            .quantity(savedInventory.getQuantity().value())
            .locationId(savedInventory.getLocationId().value())
            .brand(savedInventory.getBrand())
            .purchaseLocation(savedInventory.getPurchaseLocation())
            .occurredOn(LocalDateTime.now())
            .build();

        eventPublisher.publish(event);
        log.info("Published InventoryPurchasedEvent for new inventory: {}", savedInventory.getItemName());

        return savedInventory;
    }

}
