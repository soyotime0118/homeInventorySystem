package me.mason.management.application.ports.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

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
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 0보다 작을 수 없습니다");
        }
    }

    private void validateItemName(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("물품명은 필수입니다");
        }
    }
} 