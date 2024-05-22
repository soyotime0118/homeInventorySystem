package me.mason.management.application;

import me.mason.fake.FakeInventoryRepository;
import me.mason.management.adapter.in.web.PurchaseNewInventoryCommand;
import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.in.PurchaseInventoryInput;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryPurchaserTest {

    PurchaseInventoryInput purchaseInventoryInput;


    @Test
    void purchaseNewInventory() {

        FakeInventoryRepository fakeInventoryRepository = new FakeInventoryRepository();
        purchaseInventoryInput = new InventoryPurchaser(fakeInventoryRepository);

        PurchaseNewInventoryCommand command = new PurchaseNewInventoryCommand("test", 1, "OFFICE");
        InventoryId inventoryId = purchaseInventoryInput.purchaseNewInventory(command);

        Inventory inventory = fakeInventoryRepository.loadByInventoryId(inventoryId).get();
        System.out.println(inventory.toString());



    }
}