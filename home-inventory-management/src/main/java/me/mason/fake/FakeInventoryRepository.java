package me.mason.fake;

import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public InventoryId addInventory(String inventoryName, int inventoryQuantity, int locationId, String brandName, String purchaseLocation) {
        long nextValue = idInventoryMap.keySet()
                                 .stream()
                                 .max(Comparator.comparing(InventoryId::getValue))
                                 .map(InventoryId::getValue)
                                 .orElse(0L) + 1;
        InventoryId nextId = InventoryId.of(nextValue);
        Inventory newInventory = Inventory.withId(
                nextId,
                inventoryName,
                inventoryQuantity,
                locationId,
                brandName,
                purchaseLocation,
                LocalDateTime.now());
        idInventoryMap.put(nextId, newInventory);
        return nextId;
    }

}
