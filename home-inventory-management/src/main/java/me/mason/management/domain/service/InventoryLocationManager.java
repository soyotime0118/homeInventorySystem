package me.mason.management.domain.service;

import me.mason.management.application.ports.in.UpdateInventoryLocationInput;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryLocationManager implements UpdateInventoryLocationInput {

    private final ModifyInventoryStateOutput repository;

    @Autowired
    public InventoryLocationManager(ModifyInventoryStateOutput repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Inventory updateInventoryLocation(Long inventoryId, Long locationId) {
        return repository.updateInventoryLocation(inventoryId, locationId)
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found: " + inventoryId));
    }
}
