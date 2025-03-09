package me.mason.management.infrastructure.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import me.mason.management.domain.model.Inventory;

@Schema
public record InventoryResponse(
        String inventoryId,
        String itemName,
        long quantity,
        String brand,
        String purchaseLocation,
        String lastPurchase,
        String expiryDate
) {
    public static InventoryResponse from(Inventory inventory) {
        return new InventoryResponse(
                inventory.getInventoryId().getValue().toString(),
                inventory.getItemName(),
                inventory.getQuantity().asLong(),
                inventory.getBrand(),
                inventory.getPurchaseLocation(),
                inventory.getLastPurchase() != null ? inventory.getLastPurchase().toString() : null,
                inventory.getExpiryDate() != null ? inventory.getExpiryDate().toString() : null
        );
    }
}
