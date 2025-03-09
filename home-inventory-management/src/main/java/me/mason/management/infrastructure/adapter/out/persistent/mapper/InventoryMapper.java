package me.mason.management.infrastructure.adapter.out.persistent.mapper;

import me.mason.management.domain.model.InventoryFactory;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.infrastructure.adapter.out.persistent.entity.InventoryEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InventoryMapper {

    public Inventory mapToDomain(InventoryEntity inventoryEntity) {
        return InventoryFactory
                .withId(
                        InventoryId.of(inventoryEntity.getId()),
                        inventoryEntity.getItemName(),
                        inventoryEntity.getQuantity(),
                        inventoryEntity.getLocationId(),
                        inventoryEntity.getBrand(),
                        inventoryEntity.getPurchaseLocation(),
                        inventoryEntity.getPurchaseDate(),
                        inventoryEntity.getExpiryDate()
                );
    }

    public InventoryEntity mapToJpaEntity(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory is null");
        }
        Long id = Optional.ofNullable(inventory.getInventoryId()).map(InventoryId::getValue).orElse(null);
        return new InventoryEntity(
                id,
                inventory.getItemName(),
                inventory.getQuantity().asLong(),
                inventory.getLocationId(),
                inventory.getBrand(),
                inventory.getPurchaseLocation(),
                inventory.getLastPurchase(),
                inventory.getExpiryDate()
        );
    }
}
