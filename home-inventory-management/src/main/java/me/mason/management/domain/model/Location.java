package me.mason.management.domain.model;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Location {

    private LocationId locationId;

    private String name;

    public static Location of(Long id, String name) {
        return new Location(new LocationId(id), name);
    }

    public record LocationId(Long value) {
    }
}
