package me.mason.management.application.ports.in;

import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.exception.NotEnoughStockException;

public interface UseInventoryInput {

    /**
     * 재고 사용처리, 수량이 부족할경우 {@link NotEnoughStockException} 발생
     *
     * @param inventoryId 사용처리하는 재고ID
     * @param quantity    수량
     * @return 사용후 잔여수량
     * @throws IllegalArgumentException 수량이 0이하일 경우
     */
    long usingInventory(InventoryId inventoryId, int quantity);


}
