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
                inventoryEntity.getItemName(),
                inventoryEntity.getQuantity(),
                inventoryEntity.getLocationId(),
                inventoryEntity.getPurchaseDate());
    }

    public InventoryEntity mapToJpaEntity(Inventory inventory) {
        return new InventoryEntity(
                //todo 새로 추가하는 경우 inventoryId가 null 인데 이렇게 처리하는게 맞는지 확인필요
                inventory.getInventoryId() == null ? null : inventory.getInventoryId().getValue(),
                inventory.getItemName(),
                inventory.getQuantity(),
                inventory.getLocationId(),
                "",
                "",
                inventory.getLastPurchase(),
                inventory.getExpiryDate()
        );
    }
}
