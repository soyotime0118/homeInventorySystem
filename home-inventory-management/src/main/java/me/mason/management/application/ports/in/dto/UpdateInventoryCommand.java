package me.mason.management.application.ports.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "재고 수정을 위한 필수 정보")
public record UpdateInventoryCommand(
        long inventoryId,
        String itemName,
        Integer locationId,
        String brand,
        String purchaseLocation
) {
    @Builder
    public UpdateInventoryCommand {
    }

    public boolean hasItemName() {
        return itemName != null && !itemName.trim().isEmpty();
    }

    public boolean hasLocationId() {
        return locationId != null;
    }

    public boolean hasBrand() {
        return brand != null && !brand.trim().isEmpty();
    }

    public boolean hasPurchaseLocation() {
        return purchaseLocation != null && !purchaseLocation.trim().isEmpty();
    }
} 