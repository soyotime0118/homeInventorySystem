package me.mason.management.infrastructure.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import me.mason.management.application.ports.in.SearchInventoryInput;
import me.mason.management.domain.model.Location.LocationId;
import me.mason.management.infrastructure.adapter.in.web.response.InventoryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("inventory")
@RequiredArgsConstructor
@RestController
public class InventorySearchRestController {

    private final SearchInventoryInput searchInventoryInput;

    @Operation(summary = "상품 이름검색")
    @GetMapping("/search/like")
    public List<InventoryResponse> loadByContainingInventoryName(@RequestParam String keyword) {
        return searchInventoryInput.loadByContainingInventoryName(keyword)
                .stream().map(InventoryResponse::from).toList();
    }

    @Operation(summary = "보관 위치로 검색")
    @GetMapping("/search/by-location")
    public List<InventoryResponse> loadByLocation(@RequestParam Long locationId) {
        return searchInventoryInput.loadByLocationId(new LocationId(locationId)).stream()
                .map(InventoryResponse::from)
                .toList();
    }
}
