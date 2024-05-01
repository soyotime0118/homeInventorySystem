package me.mason.management.ports.in;

import me.mason.management.adapter.in.web.PurchaseNewInventoryCommand;
import me.mason.management.domain.Inventory.InventoryId;

public interface PurchaseInventoryInput {

    /**
     * 재고 구입처리하여, 재고수량을 증가시킴
     *
     * @param inventoryId 사용처리하는 재고ID
     * @param quantity    수량
     */
    void purchaseInventory(InventoryId inventoryId, Integer quantity);

    /**
     * 신규 재고 구입 처리
     */
    void purchaseNewInventory(PurchaseNewInventoryCommand command);
}
