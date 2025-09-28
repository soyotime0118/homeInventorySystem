package me.mason.management.application.ports.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import me.mason.management.domain.model.Inventory.Quantity;

@Schema(description = "신규 재고 등록을 위한 필수 정보")
public record RegisterInventoryCommand(
        String itemName,
        int quantity,
        int locationId,
        String brand,
        String purchaseLocation
) {
    @Builder
    public RegisterInventoryCommand {
        validateQuantity(quantity);
        validateItemName(itemName);

    }

    private void validateQuantity(int quantity) {
        Quantity.of(quantity);
    }

    private void validateItemName(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("물품명은 필수입니다");
        }
    }
} 