package me.mason.management.application.ports.in;

import me.mason.management.application.ports.in.dto.PurchaseNewInventoryCommand;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.model.Inventory.Quantity;

public interface PurchaseInventoryInput {

    /**
     * 재고 구입처리하여, 재고수량을 증가시킴
     *
     * @param inventoryId 사용처리하는 재고ID
     * @param quantity    수량
     * @return 처리된 재고
     * @throws me.mason.management.domain.exception.InvalidQuantityException quantity 가 0 이하인 경우 발생
     */
    Inventory purchaseInventory(InventoryId inventoryId, Quantity quantity);

    /**
     * 신규 재고 구입 처리
     */
    Inventory purchaseNewInventory(PurchaseNewInventoryCommand command);
}
