package me.mason.management.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.in.PurchaseInventoryInput;
import me.mason.management.ports.in.UseInventoryInput;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("inventory")
@RestController
public class InventoryRestController {

    private final UseInventoryInput useInventoryInput;

    private final PurchaseInventoryInput purchaseInventoryInput;

    @Autowired
    public InventoryRestController(UseInventoryInput useInventoryInput, PurchaseInventoryInput purchaseInventoryInput) {
        this.useInventoryInput = useInventoryInput;
        this.purchaseInventoryInput = purchaseInventoryInput;
    }


    @Operation(summary = "재고사용", description = "존재하는 재고를 사용처리 한다")
    @PatchMapping("use/{inventoryId}/{quantity}")
    public void useInventory(
            @Parameter(description = "사용하려는 재고ID") @PathVariable Long inventoryId,
            @Parameter(description = "사용수량") @PathVariable Integer quantity) {
        useInventoryInput.usingInventory(InventoryId.of(inventoryId), quantity);
    }

    @Operation(summary = "기존 재고구입", description = "존재하는 재고를 구입")
    @PutMapping("purchase/{inventoryId}/{quantity}")
    public void purchaseInventory(
            @PathVariable("inventoryId") Long inventoryId,
            @PathVariable Integer quantity) {
        purchaseInventoryInput.purchaseInventory(InventoryId.of(inventoryId), quantity);
    }

    @Operation(summary = "신규 재고구입")
    @PostMapping("purchase/")
    public void purchaseInventory(@RequestBody PurchaseNewInventoryCommand command) {
        purchaseInventoryInput.purchaseNewInventory(command);
    }
}
