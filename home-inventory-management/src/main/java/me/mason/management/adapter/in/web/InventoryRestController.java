package me.mason.management.adapter.in.web;

import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.in.UseInventoryInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("inventory")
@RestController
public class InventoryRestController {

    private final UseInventoryInput useInventoryInput;

    @Autowired
    public InventoryRestController(UseInventoryInput useInventoryInput) {
        this.useInventoryInput = useInventoryInput;
    }


    @PatchMapping("use/{inventoryId}/{quantity}")
    public void useInventory(
            @PathVariable Long inventoryId,
            @PathVariable Integer quantity) {
        useInventoryInput.usingInventory(InventoryId.of(inventoryId), quantity);
    }
}
