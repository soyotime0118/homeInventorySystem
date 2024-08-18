package me.mason.management.adapter.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import me.mason.management.adapter.out.persistent.InventoryPersistentRepository;
import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.in.PurchaseInventoryInput;
import me.mason.management.ports.in.UseInventoryInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("inventory")
@RestController
public class InventoryRestController {

    private final UseInventoryInput useInventoryInput;

    private final PurchaseInventoryInput purchaseInventoryInput;

    private final InventoryPersistentRepository inventoryPersistentRepository;

    @Autowired
    public InventoryRestController(
            UseInventoryInput useInventoryInput,
            PurchaseInventoryInput purchaseInventoryInput,
            InventoryPersistentRepository inventoryPersistentRepository) {
        this.useInventoryInput = useInventoryInput;
        this.purchaseInventoryInput = purchaseInventoryInput;
        this.inventoryPersistentRepository = inventoryPersistentRepository;
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

    @Operation(summary = "상품 이름검색")
    @GetMapping("/search/like")
    public List<Inventory> loadByContainingInventoryName(@RequestParam String keyword) {
        return inventoryPersistentRepository.loadByContainingInventoryName(keyword);
    }
}
