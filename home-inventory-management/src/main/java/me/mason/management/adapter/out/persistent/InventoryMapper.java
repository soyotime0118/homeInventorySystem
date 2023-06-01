package me.mason.management.adapter.out.persistent;

import me.mason.management.adapter.out.persistent.entity.InventoryEntity;
import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public Inventory mapToDomain(InventoryEntity inventoryEntity) {
        return Inventory.withId(
                InventoryId.of(inventoryEntity.getId()),
                inventoryEntity.getName(),
                inventoryEntity.getQuantity());
    }

    public InventoryEntity mapToJpaEntity(Inventory inventory) {
        return new InventoryEntity(
                inventory.getInventoryId().getValue(), inventory.getName(), inventory.getQuantity());
    }
}
