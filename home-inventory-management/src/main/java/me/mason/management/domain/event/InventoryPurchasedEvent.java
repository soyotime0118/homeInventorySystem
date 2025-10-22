package me.mason.management.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 신규 재고 구매 이벤트
 */
@Value
@Builder
public class InventoryPurchasedEvent implements DomainEvent {
    @JsonProperty("inventoryId")
    Long inventoryId;

    @JsonProperty("itemName")
    String itemName;

    @JsonProperty("quantity")
    Long quantity;

    @JsonProperty("locationId")
    Long locationId;

    @JsonProperty("brand")
    String brand;

    @JsonProperty("purchaseLocation")
    String purchaseLocation;

    @JsonProperty("occurredOn")
    LocalDateTime occurredOn;

    @Override
    public String eventType() {
        return "InventoryPurchased";
    }
}
