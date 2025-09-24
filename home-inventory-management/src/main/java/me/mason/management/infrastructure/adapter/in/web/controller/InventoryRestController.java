package me.mason.management.infrastructure.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import me.mason.management.application.ports.in.PurchaseInventoryInput;
import me.mason.management.application.ports.in.SearchInventoryInput;
import me.mason.management.application.ports.in.UpdateInventoryLocationInput;
import me.mason.management.application.ports.in.UseInventoryInput;
import me.mason.management.application.ports.in.dto.RegisterInventoryCommand;
import me.mason.management.application.ports.in.dto.UpdateInventoryCommand;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.service.InventoryManager;
import me.mason.management.infrastructure.adapter.in.web.command.PurchaseNewInventoryCommand;
import me.mason.management.infrastructure.adapter.in.web.response.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("inventory")
@RestController
public class InventoryRestController {

    private final UseInventoryInput useInventoryInput;

    private final PurchaseInventoryInput purchaseInventoryInput;

    private final SearchInventoryInput searchInventoryInput;

    private final UpdateInventoryLocationInput updateInventoryLocationInput;

    private final InventoryManager inventoryManager;

    @Autowired
    public InventoryRestController(
            UseInventoryInput useInventoryInput,
            PurchaseInventoryInput purchaseInventoryInput,
            SearchInventoryInput searchInventoryInput,
            UpdateInventoryLocationInput updateInventoryLocationInput,
            InventoryManager inventoryManager) {
        this.useInventoryInput = useInventoryInput;
        this.purchaseInventoryInput = purchaseInventoryInput;
        this.searchInventoryInput = searchInventoryInput;
        this.updateInventoryLocationInput = updateInventoryLocationInput;
        this.inventoryManager = inventoryManager;
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
    public List<InventoryResponse> loadByContainingInventoryName(@RequestParam String keyword) {
        return searchInventoryInput.loadByContainingInventoryName(keyword)
                .stream().map(InventoryResponse::from).toList();
    }

    @Operation(summary = "보관 위치로 검색")
    @GetMapping("/search/by-location")
    public List<InventoryResponse> loadByLocation(@RequestParam Long locationId) {
        return searchInventoryInput.loadByLocationId(locationId).stream()
                .map(InventoryResponse::from)
                .toList();
    }

    @Operation(
            summary = "신규 재고 등록",
            description = "새로운 재고 정보를 등록합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "등록된 재고 정보 반환",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = InventoryResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "잘못된 요청 파라미터"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "서버 에러"
                    )
            }
    )
    @PostMapping("/register")
    public InventoryResponse registerInventory(@Parameter @RequestBody RegisterInventoryCommand command) {
        return InventoryResponse.from(inventoryManager.registerInventory(command));
    }

    @PutMapping("/update")
    public InventoryResponse updateInventory(@Parameter @RequestBody UpdateInventoryCommand command) {
        return InventoryResponse.from(inventoryManager.updateInventory(command));
    }

    @Operation(summary = "재고 보관 위치 변경")
    @PutMapping("/{inventoryId}/location/{locationId}")
    public InventoryResponse updateInventoryLocation(
            @PathVariable Long inventoryId,
            @PathVariable Long locationId) {
        return InventoryResponse.from(updateInventoryLocationInput.updateInventoryLocation(inventoryId, locationId));
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long inventoryId) {
        inventoryManager.deleteInventory(InventoryId.of(inventoryId));
        return ResponseEntity.ok().build();
    }
}
