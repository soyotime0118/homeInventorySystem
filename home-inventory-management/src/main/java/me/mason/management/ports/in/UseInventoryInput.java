package me.mason.management.ports.in;

import me.mason.management.domain.Inventory.InventoryId;

public interface UseInventoryInput {

    /**
     * 재고 사용처리, 수량이 부족할경우 {@link me.mason.management.domain.NotEnoughStockException} 발생
     *
     * @param inventoryId 사용처리하는 재고ID
     * @param quantity    수량
     * @return 사용후 잔여수량
     */
    int usingInventory(InventoryId inventoryId, Integer quantity);


}
