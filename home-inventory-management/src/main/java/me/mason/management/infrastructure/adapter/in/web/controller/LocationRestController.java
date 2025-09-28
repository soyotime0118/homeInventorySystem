package me.mason.management.infrastructure.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import me.mason.management.application.ports.in.ManageLocationInput;
import me.mason.management.infrastructure.adapter.in.web.response.LocationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationRestController {

    private final ManageLocationInput manageLocationInput;

    @Autowired
    public LocationRestController(ManageLocationInput manageLocationInput) {
        this.manageLocationInput = manageLocationInput;
    }

    public record LocationRequest(@NotBlank String name) {}

    @Operation(summary = "보관 위치 등록", description = "새로운 보관 위치를 등록합니다.")
    @PostMapping
    public LocationResponse register(@Valid @RequestBody LocationRequest request) {
        return LocationResponse.from(manageLocationInput.registerLocation(request.name()));
    }

    @Operation(summary = "보관 위치 목록 조회", description = "등록된 모든 보관 위치를 조회합니다.")
    @GetMapping
    public List<LocationResponse> list() {
        return manageLocationInput.listLocations().stream()
                .map(LocationResponse::from)
                .toList();
    }
}
