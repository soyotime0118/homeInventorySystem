package me.mason.management.application.ports.in;

import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.model.Location.LocationId;

public interface UpdateInventoryLocationInput {
    Inventory updateInventoryLocation(InventoryId inventoryId, LocationId locationId);
}
