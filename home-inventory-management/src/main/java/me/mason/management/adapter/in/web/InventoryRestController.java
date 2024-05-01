package me.mason.management.adapter.in.web;

import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.in.PurchaseInventoryInput;
import me.mason.management.ports.in.UseInventoryInput;
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


    @PatchMapping("use/{inventoryId}/{quantity}")
    public void useInventory(
            @PathVariable Long inventoryId,
            @PathVariable Integer quantity) {
        useInventoryInput.usingInventory(InventoryId.of(inventoryId), quantity);
    }

    @PutMapping("purchase/{inventoryId}/{quantity}")
    public void purchaseInventory(
            @PathVariable("inventoryId") Long inventoryId,
            @PathVariable Integer quantity) {
        purchaseInventoryInput.purchaseInventory(InventoryId.of(inventoryId), quantity);
    }

    @PostMapping("purchase/")
    public void purchaseInventory(@RequestBody PurchaseNewInventoryCommand command) {
        purchaseInventoryInput.purchaseNewInventory(command);
    }
}
