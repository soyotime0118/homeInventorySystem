package me.mason.management.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Getter
@AllArgsConstructor
public class Inventory {

    private InventoryId inventoryId;

    private String name;

    private int quantity;


    /**
     * {@link InventoryId } 가 존재 하는 경우 사용 하는 Builder, 주로 저장소에서 객체를 로드하는경우 호출
     *
     * @param inventoryId 저장된 InventoryId
     * @param name        인벤토리 이름
     * @param quantity    수량
     */
    @Builder(builderMethodName = "withId")
    public static Inventory withId(
            InventoryId inventoryId,
            String name,
            int quantity) {
        return new Inventory(inventoryId, name, quantity);
    }

    /**
     * {@link InventoryId } 가 존재 하는 경우 사용 하는 Builder, 주로 저장소에서 객체를 로드하는경우 호출
     *
     * @param name     인벤토리 이름
     * @param quantity 수량
     */
    @Builder(builderMethodName = "withoutId")
    public static Inventory withoutId(
            String name,
            int quantity) {
        return new Inventory(null, name, quantity);
    }

    /**
     * 재고 사용처리
     *
     * @param useQuantity 사용 수량
     */
    public void use(int useQuantity) {
        if (quantity - useQuantity < 0) {
            throw new NotEnoughStockException(inventoryId, useQuantity);
        }
        quantity = quantity - useQuantity;
    }


    @Value(staticConstructor = "of")
    public static class InventoryId {
        Long value;
    }
}
