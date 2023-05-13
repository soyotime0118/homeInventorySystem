package me.mason.management.domain;

import me.mason.management.domain.Inventory.InventoryId;

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException(InventoryId inventoryId, int quantity) {
        super(String.format("재고가 부족합니다. 상품ID %s, 부족수량 %d ", inventoryId.toString(), quantity));
    }
}
