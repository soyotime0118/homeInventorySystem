package me.mason.management.domain.model;

import lombok.*;
import me.mason.management.domain.exception.NotEnoughStockException;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Inventory {

    private InventoryId inventoryId;

    private String itemName;

    private int quantity;

    private int locationId;

    private String brand;

    private String purchaseLocation;

    private LocalDateTime lastPurchase;

    private LocalDateTime expiryDate;

    @Builder(toBuilder = true)
    private Inventory(
            InventoryId inventoryId,
            String itemName,
            int quantity,
            int locationId,
            String brand,
            String purchaseLocation,
            LocalDateTime lastPurchase,
            LocalDateTime expiryDate
    ) {
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.locationId = locationId;
        this.brand = brand;
        this.purchaseLocation = purchaseLocation;
        this.lastPurchase = lastPurchase;
        this.expiryDate = expiryDate;
    }

    /**
     * {@link InventoryId } 가 존재 하는 경우 사용 하는 Builder, 주로 저장소에서 객체를 로드하는경우 호출
     *
     * @param inventoryId 저장된 InventoryId
     * @param name        인벤토리 이름
     * @param quantity    수량
     */
    public static Inventory withId(
            InventoryId inventoryId,
            String name,
            int quantity,
            int locationId,
            String brandName,
            String purchaseLocation,
            LocalDateTime lastPurchase
    ) {
        validateQuantity(quantity);
        return Inventory.builder()
                .inventoryId(inventoryId)
                .itemName(name)
                .quantity(quantity)
                .locationId(locationId)
                .brand(brandName)
                .purchaseLocation(purchaseLocation)
                .lastPurchase(lastPurchase)
                .build();
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater than -1");
        }
    }

    /**
     * {@link InventoryId } 가 존재 하는 경우 사용 하는 Builder, 주로 저장소에서 객체를 로드하는경우 호출
     *
     * @param name     인벤토리 이름
     * @param quantity 수량
     * @throws IllegalArgumentException quantity가 음수인경우 발생
     */
    public static Inventory withoutId(
            String name,
            int quantity,
            int locationId,
            String brandName,
            String purchaseLocation,
            LocalDateTime lastPurchase
    ) {
        validateQuantity(quantity);
        return Inventory.builder()
                .itemName(name)
                .quantity(quantity)
                .locationId(locationId)
                .brand(brandName)
                .purchaseLocation(purchaseLocation)
                .lastPurchase(lastPurchase)
                .build();
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
        return locationId;
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
