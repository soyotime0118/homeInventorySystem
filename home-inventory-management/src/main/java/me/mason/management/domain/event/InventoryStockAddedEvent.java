package me.mason.management.domain.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 기존 재고 수량 추가 이벤트
 */
@Value
@Builder
public class InventoryStockAddedEvent implements DomainEvent {
    @JsonProperty("inventoryId")
    Long inventoryId;

    @JsonProperty("itemName")
    String itemName;

    @JsonProperty("addedQuantity")
    Long addedQuantity;

    @JsonProperty("totalQuantity")
    Long totalQuantity;

    @JsonProperty("occurredOn")
    LocalDateTime occurredOn;

    @Override
    public String eventType() {
        return "InventoryStockAdded";
    }
}
