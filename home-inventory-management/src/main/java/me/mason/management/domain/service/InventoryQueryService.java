package me.mason.management.domain.service;

import me.mason.management.application.ports.in.SearchInventoryInput;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.model.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryQueryService implements SearchInventoryInput {

    private final ModifyInventoryStateOutput repository;

    @Autowired
    public InventoryQueryService(ModifyInventoryStateOutput repository) {
        this.repository = repository;
    }

    @Override
    public List<Inventory> loadByContainingInventoryName(String keyword) {
        return repository.loadByContainingInventoryName(keyword);
    }

    @Override
    public List<Inventory> loadByLocationId(Long locationId) {
        return repository.loadByLocationId(locationId);
    }
}
