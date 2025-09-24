package me.mason.management.infrastructure.adapter.in.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import me.mason.management.infrastructure.adapter.in.web.response.LocationResponse;
import me.mason.management.infrastructure.adapter.out.persistent.LocationPersistentAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationRestController {

    private final LocationPersistentAdapter locationPersistentAdapter;

    @Autowired
    public LocationRestController(LocationPersistentAdapter locationPersistentAdapter) {
        this.locationPersistentAdapter = locationPersistentAdapter;
    }

    public static record LocationRequest(String name) {}

    @Operation(summary = "보관 위치 등록")
    @PostMapping
    public LocationResponse register(@RequestBody LocationRequest request) {
        return LocationResponse.from(locationPersistentAdapter.registerLocation(request.name()));
    }

    @Operation(summary = "보관 위치 목록 조회")
    @GetMapping
    public List<LocationResponse> list() {
        return locationPersistentAdapter.findAll().stream()
                .map(LocationResponse::from)
                .toList();
    }
}
