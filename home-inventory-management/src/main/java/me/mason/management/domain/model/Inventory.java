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
     * {@link InventoryId }가 존재하는 경우 사용하는 Builder, 주로 저장소에서 객체를 로드할 때 호출
     *
     * @param inventoryId      저장된 InventoryId
     * @param itemName         인벤토리 이름
     * @param quantity         수량
     * @param locationId       위치 ID
     * @param brandName        브랜드 이름
     * @param purchaseLocation 구매 위치
     * @param lastPurchase     마지막 구매 일시
     * @return 새로운 Inventory 객체
     */
    public static Inventory withId(
            InventoryId inventoryId,
            String itemName,
            int quantity,
            int locationId,
            String brandName,
            String purchaseLocation,
            LocalDateTime lastPurchase
    ) {
        validateQuantity(quantity);
        return Inventory.builder()
                .inventoryId(inventoryId)
                .itemName(itemName)
                .quantity(quantity)
                .locationId(locationId)
                .brand(brandName)
                .purchaseLocation(purchaseLocation)
                .lastPurchase(lastPurchase)
                .build();
    }

    /**
     * {@link InventoryId }가 존재하지 않는 경우 사용하는 Builder, 주로 새로운 객체를 생성할 때 호출
     *
     * @param itemName         인벤토리 이름
     * @param quantity         수량
     * @param locationId       위치 ID
     * @param brandName        브랜드 이름
     * @param purchaseLocation 구매 위치
     * @param lastPurchase     마지막 구매 일시
     * @return 새로운 Inventory 객체
     * @throws IllegalArgumentException quantity가 음수일 경우 발생
     */
    public static Inventory withoutId(
            String itemName,
            int quantity,
            int locationId,
            String brandName,
            String purchaseLocation,
            LocalDateTime lastPurchase
    ) {
        validateQuantity(quantity);
        return Inventory.builder()
                .itemName(itemName)
                .quantity(quantity)
                .locationId(locationId)
                .brand(brandName)
                .purchaseLocation(purchaseLocation)
                .lastPurchase(lastPurchase)
                .build();
    }

    /**
     * 수량을 검증하는 메서드
     *
     * @param quantity 검증할 수량
     * @throws IllegalArgumentException 수량이 0보다 작으면 예외 발생
     */
    private static void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be greater than -1");
        }
    }

    /**
     * 재고 사용 처리
     *
     * @param useQuantity 사용할 수량
     * @throws NotEnoughStockException 재고가 부족할 경우 발생
     */
    public void use(int useQuantity) {
        if (quantity - useQuantity < 0) {
            throw new NotEnoughStockException(inventoryId, useQuantity);
        }
        quantity -= useQuantity;
    }

    /**
     * 추가 재고 처리
     *
     * @param quantity 추가할 수량
     */
    public void plusStock(Integer quantity) {
        this.quantity += quantity;
        this.lastPurchase = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Inventory{" +
               "inventoryId=" + inventoryId +
               ", itemName='" + itemName + '\'' +
               ", quantity=" + quantity +
               ", lastPurchase=" + lastPurchase +
               '}';
    }

    @Value(staticConstructor = "of")
    public static class InventoryId {
        Long value;
    }
}