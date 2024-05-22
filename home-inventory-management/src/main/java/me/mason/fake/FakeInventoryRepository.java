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
    public InventoryId addInventory(Inventory inventory) {
        InventoryId nextId;
        if (inventory.getInventoryId() == null) {
            if (idInventoryMap.isEmpty()) {
                nextId = InventoryId.of(1L);
            } else {
                nextId = InventoryId.of(idInventoryMap.keySet().stream().max(Comparator.comparing(InventoryId::getValue)).get().getValue() + 1);
            }
            inventory = Inventory.withId(nextId, inventory.getName(), inventory.getQuantity(), inventory.getLastPurchase());
            idInventoryMap.put(nextId, inventory);
        } else {
            nextId = inventory.getInventoryId();
        }
        return nextId;
    }

    public void addData(InventoryId id, String name, int quantity, LocalDateTime lastPurchase) {
        idInventoryMap.put(id, Inventory.withId(id, name, quantity, lastPurchase));
    }
}
