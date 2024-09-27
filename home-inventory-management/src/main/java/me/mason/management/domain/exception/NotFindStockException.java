package me.mason.management.domain.exception;

import me.mason.management.domain.model.Inventory.InventoryId;

public class NotFindStockException extends RuntimeException {

    public NotFindStockException(InventoryId inventoryId) {
        super(String.format("재고를 찾을 수 없습니다. %s", inventoryId));
    }
}
