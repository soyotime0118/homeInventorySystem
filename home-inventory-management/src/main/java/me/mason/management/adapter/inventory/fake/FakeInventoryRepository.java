package me.mason.management.adapter.inventory.fake;

import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.out.ModifyInventoryStateOutput;

import java.util.HashMap;
import java.util.Map;

//@Service
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
    public Inventory loadByInventoryId(InventoryId inventoryId) {
        return idInventoryMap.get(inventoryId);
    }

    public void addData(InventoryId id, String name, int quantity) {
        idInventoryMap.put(id, Inventory.withId(id, name, quantity));
    }
}
