package me.mason.fake;

import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;

import java.util.*;
import java.util.stream.Collectors;

public class FakeInventoryRepository implements ModifyInventoryStateOutput {

    private Map<InventoryId, Inventory> idInventoryMap = new HashMap<>();

    public FakeInventoryRepository() {
    }

    public FakeInventoryRepository(Map<InventoryId, Inventory> idInventoryMap) {
        this.idInventoryMap = idInventoryMap;
    }

    @Override
    public void modifyInventory(Inventory inventory) {
        idInventoryMap.put(inventory.getInventoryId(), inventory);
    }


    @Override
    public Optional<Inventory> loadByInventoryId(InventoryId inventoryId) {
        return Optional.ofNullable(idInventoryMap.get(inventoryId));
    }

    @Override
    public Inventory addInventory(Inventory createdInventory) {
        long nextValue = idInventoryMap.keySet()
                                 .stream()
                                 .max(Comparator.comparing(InventoryId::getValue))
                                 .map(InventoryId::getValue)
                                 .orElse(0L) + 1;
        InventoryId nextId = InventoryId.of(nextValue);
        Inventory newInventory = createdInventory.toBuilder().inventoryId(nextId).build();
        idInventoryMap.put(nextId, newInventory);
        return newInventory;
    }

    @Override
    public Inventory updateInventory(Inventory inventory) {
        idInventoryMap.put(inventory.getInventoryId(), inventory);
        return inventory;
    }

    @Override
    public void deleteInventory(InventoryId inventoryId) {
        idInventoryMap.remove(inventoryId);
    }

    @Override
    public boolean existsById(InventoryId inventoryId) {
        return idInventoryMap.containsKey(inventoryId);
    }

    @Override
    public List<Inventory> loadByContainingInventoryName(String inventoryName) {
        return idInventoryMap.values().stream()
                .filter(inv -> inv.getItemName() != null && inv.getItemName().contains(inventoryName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Inventory> loadByLocationId(Long locationId) {
        return idInventoryMap.values().stream()
                .filter(inv -> inv.getLocationId() == Optional.ofNullable(locationId).orElse(-1L))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Inventory> updateInventoryLocation(Long inventoryId, Long locationId) {
        InventoryId id = InventoryId.of(inventoryId);
        Inventory current = idInventoryMap.get(id);
        if (current == null) {
            return Optional.empty();
        }
        Inventory updated = current.toBuilder().locationId(locationId).build();
        idInventoryMap.put(id, updated);
        return Optional.of(updated);
    }

    @Override
    public List<Inventory> findAll() {
        return new ArrayList<>(idInventoryMap.values());
    }
}
