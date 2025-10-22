package me.mason.management.infrastructure.adapter.out.event;

import me.mason.management.domain.event.InventoryPurchasedEvent;
import me.mason.management.domain.event.InventoryStockAddedEvent;
import me.mason.management.domain.event.InventoryUsedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Kafka 이벤트 퍼블리셔 통합 테스트
 */
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"inventory-events"})
class KafkaEventPublisherTest {

    @Autowired
    private KafkaEventPublisher kafkaEventPublisher;

    @Test
    @DisplayName("신규 재고 구매 이벤트 발행 성공")
    void shouldPublishInventoryPurchasedEvent() {
        // given
        InventoryPurchasedEvent event = InventoryPurchasedEvent.builder()
            .inventoryId(1L)
            .itemName("Test Item")
            .quantity(10L)
            .locationId(1L)
            .brand("Test Brand")
            .purchaseLocation("Test Store")
            .occurredOn(LocalDateTime.now())
            .build();

        // when & then
        assertDoesNotThrow(() -> kafkaEventPublisher.publish(event));
    }

    @Test
    @DisplayName("재고 수량 추가 이벤트 발행 성공")
    void shouldPublishInventoryStockAddedEvent() {
        // given
        InventoryStockAddedEvent event = InventoryStockAddedEvent.builder()
            .inventoryId(1L)
            .itemName("Test Item")
            .addedQuantity(5L)
            .totalQuantity(15L)
            .occurredOn(LocalDateTime.now())
            .build();

        // when & then
        assertDoesNotThrow(() -> kafkaEventPublisher.publish(event));
    }

    @Test
    @DisplayName("재고 사용 이벤트 발행 성공")
    void shouldPublishInventoryUsedEvent() {
        // given
        InventoryUsedEvent event = InventoryUsedEvent.builder()
            .inventoryId(1L)
            .itemName("Test Item")
            .usedQuantity(3L)
            .remainingQuantity(12L)
            .occurredOn(LocalDateTime.now())
            .build();

        // when & then
        assertDoesNotThrow(() -> kafkaEventPublisher.publish(event));
    }
}
