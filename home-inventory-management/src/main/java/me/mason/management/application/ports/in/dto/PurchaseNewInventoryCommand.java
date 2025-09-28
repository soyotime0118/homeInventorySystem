package me.mason.management.application.ports.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Schema(description = "신규재고구입 요청")
@Getter
@NoArgsConstructor
public class PurchaseNewInventoryCommand {
    @Schema(description = "상품명", example = "드라이버")
    private String name;
    @Schema(description = "상품수량", example = "1")
    @Min(value = 1, message = "상품수량은 0이상이어야 합니다")
    private int quantity;
    @Schema(description = "상품장소Id", example = "창고")
    private int locationId;

    private String purchaseLocation;

    private String brandName;

    public PurchaseNewInventoryCommand(
            String name,
            int quantity,
            int locationId) {
        this.name = name;
        this.quantity = quantity;
        this.locationId = locationId;
    }

}
