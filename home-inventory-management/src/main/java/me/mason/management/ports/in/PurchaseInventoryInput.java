package me.mason.management.ports.in;

import me.mason.management.adapter.in.web.PurchaseNewInventoryCommand;
import me.mason.management.domain.Inventory.InventoryId;

public interface PurchaseInventoryInput {

    /**
     * 재고 구입처리하여, 재고수량을 증가시킴
     *
     * @param inventoryId 사용처리하는 재고ID
     * @param quantity    수량
     * @throws IllegalArgumentException quantity 가 0 이하인 경우 발생
     */
    void purchaseInventory(InventoryId inventoryId, int quantity);

    /**
     * 신규 재고 구입 처리
     */
    InventoryId purchaseNewInventory(PurchaseNewInventoryCommand command);
}
