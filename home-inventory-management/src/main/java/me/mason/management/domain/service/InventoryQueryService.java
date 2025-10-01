package me.mason.management.domain.service;

import me.mason.management.application.ports.in.SearchInventoryInput;
import me.mason.management.application.ports.out.LocationOutput;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Location.LocationId;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class InventoryQueryService implements SearchInventoryInput {

    private final ModifyInventoryStateOutput repository;

    private final LocationOutput locationOutput;


    @Autowired
    public InventoryQueryService(
            ModifyInventoryStateOutput repository,
            LocationOutput locationOutput) {
        this.repository = repository;
        this.locationOutput = locationOutput;
    }

    @Override
    public List<Inventory> loadByContainingInventoryName(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return repository.findAll();
        } else {
            return repository.loadByContainingInventoryName(keyword);
        }
    }


    @Override
    public List<Inventory> loadByLocationId(LocationId locationId) {

        if (locationId == null || !locationOutput.existsById(locationId)) {
            return Collections.emptyList();
        }

        List<Inventory> inventories = repository.loadByLocationId(locationId);
        if (inventories.isEmpty()) {
            return Collections.emptyList();
        } else {
            return inventories;
        }
    }
}
