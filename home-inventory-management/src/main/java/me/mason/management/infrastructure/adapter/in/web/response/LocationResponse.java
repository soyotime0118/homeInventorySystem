package me.mason.management.infrastructure.adapter.in.web.response;

import me.mason.management.domain.model.Location;

public record LocationResponse(
        Long id,
        String name
) {

    public static LocationResponse from(Location location) {
        return new LocationResponse(
                location.getLocationId() != null ? location.getLocationId().value() : null,
                location.getName()
        );
    }
}
