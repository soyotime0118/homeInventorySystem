package me.mason.management.application.ports.out;

import me.mason.management.domain.model.Location;

import java.util.List;
import java.util.Optional;

/**
 * Output port for Location persistence.
 */
public interface LocationOutput {
    Location registerLocation(String name);

    List<Location> findAll();

    Optional<Location> getOrCreateByName(String name);
}
