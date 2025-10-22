package me.mason.management.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 재고 사용 이벤트
 */
@Value
@Builder
public class InventoryUsedEvent implements DomainEvent {
    @JsonProperty("inventoryId")
    Long inventoryId;

    @JsonProperty("itemName")
    String itemName;

    @JsonProperty("usedQuantity")
    Long usedQuantity;

    @JsonProperty("remainingQuantity")
    Long remainingQuantity;

    @JsonProperty("occurredOn")
    LocalDateTime occurredOn;

    @Override
    public String eventType() {
        return "InventoryUsed";
    }
}
