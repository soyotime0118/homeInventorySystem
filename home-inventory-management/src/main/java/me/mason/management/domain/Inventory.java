package me.mason.management.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Inventory {

    private InventoryId inventoryId;

    private String name;

    private int quantity;

    private String location;

    private LocalDateTime lastPurchase;


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
            int quantity,
            String location,
            LocalDateTime lastPurchase) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater than -1");
        }
        return new Inventory(inventoryId, name, quantity, location, lastPurchase);
    }

    /**
     * {@link InventoryId } 가 존재 하는 경우 사용 하는 Builder, 주로 저장소에서 객체를 로드하는경우 호출
     *
     * @param name     인벤토리 이름
     * @param quantity 수량
     * @throws IllegalArgumentException quantity가 음수인경우 발생
     */
    @Builder(builderMethodName = "withoutId")
    public static Inventory withoutId(
            String name,
            int quantity,
            String location,
            LocalDateTime lastPurchase) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater than -1");
        }
        return new Inventory(null, name, quantity,location, lastPurchase);
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

    public void plusStock(Integer quantity) {
        this.quantity += quantity;
        this.lastPurchase = LocalDateTime.now();
    }


    @Value(staticConstructor = "of")
    public static class InventoryId {
        Long value;
    }

    @Override
    public String toString() {
        return "Inventory{" +
               "inventoryId=" + inventoryId +
               ", name='" + name + '\'' +
               ", quantity=" + quantity +
               ", lastPurchase=" + lastPurchase +
               '}';
    }
}
