package me.mason.fake;

import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.out.ModifyInventoryStateOutput;

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
    public void addInventory(Inventory inventory) {
        if (inventory.getInventoryId() == null) {
            if (idInventoryMap.isEmpty()) {
                idInventoryMap.put(InventoryId.of(1L), inventory);
            } else {
                long nextId = idInventoryMap.keySet().stream().min(Comparator.comparing(InventoryId::getValue)).get().getValue();
                idInventoryMap.put(InventoryId.of(nextId), inventory);
            }
        }
    }

    public void addData(InventoryId id, String name, int quantity, LocalDateTime lastPurchase) {
        idInventoryMap.put(id, Inventory.withId(id, name, quantity, lastPurchase));
    }
}
