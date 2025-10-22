package me.mason.management.infrastructure.adapter.out.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mason.management.application.ports.out.EventPublisher;
import me.mason.management.domain.event.DomainEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * Kafka를 통한 도메인 이벤트 발행 어댑터
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.inventory-events}")
    private String topicName;

    @Override
    public void publish(DomainEvent event) {
        try {
            log.info("Publishing event to Kafka: type={}, topic={}", event.eventType(), topicName);

            ListenableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topicName, event.eventType(), event);

            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
                @Override
                public void onSuccess(SendResult<String, Object> result) {
                    log.info("Successfully published event: type={}, offset={}, partition={}",
                        event.eventType(),
                        result.getRecordMetadata().offset(),
                        result.getRecordMetadata().partition());
                }

                @Override
                public void onFailure(Throwable ex) {
                    log.error("Failed to publish event: type={}, error={}",
                        event.eventType(),
                        ex.getMessage(),
                        ex);
                }
            });
        } catch (Exception e) {
            log.error("Error publishing event to Kafka: type={}, error={}",
                event.eventType(),
                e.getMessage(),
                e);
            throw new RuntimeException("Failed to publish event to Kafka", e);
        }
    }
}
