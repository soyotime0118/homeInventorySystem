package me.mason.management.application.ports.in;

import me.mason.management.domain.model.Location;

import java.util.List;

/**
 * Location use-cases exposed to the web adapter.
 */
public interface ManageLocationInput {

    /**
     * Register a new storage location by name.
     *
     * @param name location name (must be non-blank)
     * @return created Location domain object
     */
    Location registerLocation(String name);

    /**
     * Load all storage locations.
     */
    List<Location> listLocations();
}
