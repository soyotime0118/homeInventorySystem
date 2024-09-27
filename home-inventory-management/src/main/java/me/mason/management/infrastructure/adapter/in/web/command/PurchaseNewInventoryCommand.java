package me.mason.management.infrastructure.adapter.in.web.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "신규재고구입 요청")
@Getter
@NoArgsConstructor
public class PurchaseNewInventoryCommand {
    @Schema(description = "상품명", example = "드라이버")
    private String name;
    @Schema(description = "상품수량", example = "1")
    private int quantity;
    @Schema(description = "상품장소Id", example = "창고")
    private int locationId;

    public PurchaseNewInventoryCommand(
            String name,
            int quantity,
            int locationId) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        this.name = name;
        this.quantity = quantity;
        this.locationId = locationId;
    }

}
