package me.mason.management.domain.model;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Location {

    private LocationId locationId;

    private String name;

    @Value(staticConstructor = "of")
    public static class LocationId {
        Long value;
    }
}
