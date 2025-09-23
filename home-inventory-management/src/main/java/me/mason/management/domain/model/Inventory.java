package me.mason.management.domain.model;

import lombok.*;
import me.mason.management.domain.exception.NotEnoughStockException;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Inventory {
    private InventoryId inventoryId;
    private String itemName;
    private Quantity quantity;
    private long locationId;
    private String brand;
    private String purchaseLocation;
    private LocalDateTime lastPurchase;
    private LocalDateTime lastUsedDate;
    private LocalDateTime expiryDate;

    @Builder(toBuilder = true)
    private Inventory(
            InventoryId inventoryId,
            String itemName,
            Quantity quantity,
            long locationId,
            String brand,
            String purchaseLocation,
            LocalDateTime lastPurchase,
            LocalDateTime lastUsedDate,
            LocalDateTime expiryDate
    ) {
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.locationId = locationId;
        this.brand = brand;
        this.purchaseLocation = purchaseLocation;
        this.lastPurchase = lastPurchase;
        this.lastUsedDate = lastUsedDate;
        this.expiryDate = expiryDate;
    }


    /**
     * 재고 사용 처리
     *
     * @param useQuantity 사용할 수량
     * @throws NotEnoughStockException 재고가 부족할 경우 발생
     */
    public void use(int useQuantity) {
        if (isExpired()) {
            throw new IllegalStateException("");
        }
        Quantity requestedQuantity = Quantity.of(useQuantity);
        if (this.quantity.isLessThan(requestedQuantity)) {
            throw new NotEnoughStockException(inventoryId, useQuantity);
        }
        this.quantity = this.quantity.subtract(Quantity.of(useQuantity));
        this.lastUsedDate = LocalDateTime.now();
    }

    /**
     * 추가 재고 처리
     *
     * @param quantity 추가할 수량
     */
    public void plusStock(Integer quantity) {
        this.quantity = this.quantity.add(Quantity.of(quantity));
        this.lastPurchase = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean isExpiringSoon(int daysThreshold) {
        return LocalDateTime.now().plusDays(daysThreshold).isAfter(expiryDate);
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

    public void changeDetail(
            String itemName,
            String brand,
            String purchaseLocation,
            Integer locationId
    ) {
        this.itemName = itemName;
        this.brand = brand;
        this.purchaseLocation = purchaseLocation;
        this.locationId = locationId;
    }

    @Value(staticConstructor = "of")
    public static class InventoryId {
        Long value;
    }


    public Quantity getQuantity() {
        return Optional.ofNullable(quantity).orElse(Quantity.ZERO);
    }

    public record Quantity(long value) {

        public static final Quantity ZERO = Quantity.of(0);

        public Quantity {
            if (value < 0) {
                throw new IllegalArgumentException("수량은 0보다 작을 수 없습니다");
            }
        }

        public long asLong() {
            return value;
        }

        @Override
        public long value() {
            return asLong();
        }

        public static Quantity of(long value) {
            return new Quantity(value);
        }

        public Quantity add(Quantity quantity) {
            return new Quantity(this.value + quantity.value);
        }

        public Quantity subtract(Quantity quantity) {
            if (this.value < quantity.value) {
                throw new IllegalArgumentException("재고가 부족합니다");
            }
            return new Quantity(this.value - quantity.value);
        }

        public boolean isLessThan(Quantity other) {
            return this.value < other.value;
        }
    }
}