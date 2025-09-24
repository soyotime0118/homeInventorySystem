package me.mason.management.application.ports.in;

import me.mason.management.domain.model.Inventory;

public interface UpdateInventoryLocationInput {
    Inventory updateInventoryLocation(Long inventoryId, Long locationId);
}
