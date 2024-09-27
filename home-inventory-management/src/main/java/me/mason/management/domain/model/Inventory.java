package me.mason.management.domain.model;

import lombok.*;
import me.mason.management.domain.exception.NotEnoughStockException;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Inventory {

    private InventoryId inventoryId;

    private String itemName;

    private int quantity;

    private int locationId;

    private LocalDateTime lastPurchase;

    private LocalDateTime expiryDate;


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
            int locationId,
            LocalDateTime lastPurchase) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater than -1");
        }
        return new Inventory(inventoryId, name, quantity, locationId, lastPurchase, null);
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
            int locationId,
            LocalDateTime lastPurchase) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater than -1");
        }
        return new Inventory(null, name, quantity, locationId, lastPurchase, null);
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

    public Integer getLocationId() {
        return null;
    }


    @Value(staticConstructor = "of")
    public static class InventoryId {
        Long value;
    }

    @Override
    public String toString() {
        return "Inventory{" +
               "inventoryId=" + inventoryId +
               ", name='" + itemName + '\'' +
               ", quantity=" + quantity +
               ", lastPurchase=" + lastPurchase +
               '}';
    }
}
