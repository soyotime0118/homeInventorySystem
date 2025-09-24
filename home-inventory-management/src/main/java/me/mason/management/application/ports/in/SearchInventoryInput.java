package me.mason.management.application.ports.in;

import me.mason.management.domain.model.Inventory;

import java.util.List;

public interface SearchInventoryInput {
    List<Inventory> loadByContainingInventoryName(String keyword);

    List<Inventory> loadByLocationId(Long locationId);
}
