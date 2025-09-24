package me.mason.management.infrastructure.adapter.in.web.response;

import me.mason.management.infrastructure.adapter.out.persistent.entity.LocationEntity;

public record LocationResponse(
        Long id,
        String name
) {
    public static LocationResponse from(LocationEntity entity) {
        return new LocationResponse(entity.getId(), entity.getLocationName());
    }
}
