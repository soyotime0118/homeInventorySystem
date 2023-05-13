package me.mason.management.ports.in;

import me.mason.management.domain.Inventory.InventoryId;

public class NotFindStockException extends RuntimeException {

    public NotFindStockException(InventoryId inventoryId) {
        super(String.format("재고를 찾을 수 없습니다. %s", inventoryId));
    }
}
