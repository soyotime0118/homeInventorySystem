package me.mason.management.domain.model;

import lombok.*;
import me.mason.management.domain.exception.InvalidQuantityException;
import me.mason.management.domain.exception.NotEnoughStockException;
import me.mason.management.domain.model.Location.LocationId;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Inventory {
    private InventoryId inventoryId;
    private String itemName;
    private Quantity quantity;
    private LocationId locationId;
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
            LocationId locationId,
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
    public void use(Quantity useQuantity) {
        if (isExpired()) {
            throw new IllegalStateException("");
        }
        if (this.quantity.isLessThan(useQuantity)) {
            throw new NotEnoughStockException(useQuantity.asLong());
        }
        this.quantity = this.quantity.subtract(useQuantity);
        this.lastUsedDate = LocalDateTime.now();
    }

    /**
     * 추가 재고 처리
     *
     * @param quantity 추가할 수량
     */
    public void plusStock(Quantity quantity) {
        this.quantity = this.quantity.add(quantity);
        this.lastPurchase = LocalDateTime.now();
    }

    public boolean isExpired() {
        if (expiryDate == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public boolean isExpiringSoon(int daysThreshold) {
        if (expiryDate == null) {
            return false;
        }
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
            LocationId locationId,
            LocalDateTime purchaseDate,
            LocalDateTime expiryDate
    ) {
        this.itemName = itemName;
        this.brand = brand;
        this.purchaseLocation = purchaseLocation;
        this.locationId = locationId;
        if (purchaseDate != null) {
            this.lastPurchase = purchaseDate;
        }
        if (expiryDate != null) {
            this.expiryDate = expiryDate;
        }
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
                throw new InvalidQuantityException("수량은 0이상이어야 합니다");
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
                throw new NotEnoughStockException(quantity.value - this.value);
            }
            return new Quantity(this.value - quantity.value);
        }

        public boolean isLessThan(Quantity other) {
            return this.value < other.value;
        }
    }
}