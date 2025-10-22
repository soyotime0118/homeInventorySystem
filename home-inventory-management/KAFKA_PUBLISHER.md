# Kafka Event Publisher

## 개요
재고 관리 시스템에서 발생하는 주요 도메인 이벤트를 Kafka를 통해 발행하는 기능입니다.

## 아키텍처

```
┌─────────────────────────────────────────────┐
│        Domain Service Layer                 │
│  - InventoryPurchaser                       │
│  - UseInventory                             │
└──────────────┬──────────────────────────────┘
               │ uses
┌──────────────▼──────────────────────────────┐
│    Application Port (Output)                │
│  - EventPublisher                           │
└──────────────┬──────────────────────────────┘
               │ implemented by
┌──────────────▼──────────────────────────────┐
│    Infrastructure Adapter                   │
│  - KafkaEventPublisher                      │
└──────────────┬──────────────────────────────┘
               │
┌──────────────▼──────────────────────────────┐
│         Apache Kafka                        │
│  Topic: inventory-events                    │
└─────────────────────────────────────────────┘
```

## 도메인 이벤트

### 1. InventoryPurchasedEvent
신규 재고 구매 시 발생하는 이벤트

**필드:**
- `inventoryId`: 재고 ID
- `itemName`: 상품명
- `quantity`: 구매 수량
- `locationId`: 보관 위치 ID
- `brand`: 브랜드
- `purchaseLocation`: 구매처
- `occurredOn`: 이벤트 발생 시각

**발행 시점:** `InventoryPurchaser.purchaseNewInventory()` 실행 후

### 2. InventoryStockAddedEvent
기존 재고 수량 추가 시 발생하는 이벤트

**필드:**
- `inventoryId`: 재고 ID
- `itemName`: 상품명
- `addedQuantity`: 추가된 수량
- `totalQuantity`: 총 수량
- `occurredOn`: 이벤트 발생 시각

**발행 시점:** `InventoryPurchaser.purchaseInventory()` 실행 후

### 3. InventoryUsedEvent
재고 사용 시 발생하는 이벤트

**필드:**
- `inventoryId`: 재고 ID
- `itemName`: 상품명
- `usedQuantity`: 사용된 수량
- `remainingQuantity`: 남은 수량
- `occurredOn`: 이벤트 발생 시각

**발행 시점:** `UseInventory.usingInventory()` 실행 후

## 설정

### application.yml
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

kafka:
  topics:
    inventory-events: inventory-events
```

### Kafka 서버 실행
```bash
# Kafka 서버 시작 (Docker 사용 예시)
docker run -d \
  --name kafka \
  -p 9092:9092 \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  wurstmeister/kafka

# Zookeeper 시작 (Kafka 의존성)
docker run -d \
  --name zookeeper \
  -p 2181:2181 \
  wurstmeister/zookeeper
```

## 의존성

`build.gradle.kts`에 다음 의존성이 추가되어 있습니다:

```kotlin
implementation("org.springframework.kafka:spring-kafka")
implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.2")
testImplementation("org.springframework.kafka:spring-kafka-test")
```

## 사용 예시

### 이벤트 발행
도메인 서비스에서 자동으로 이벤트가 발행됩니다:

```java
@Service
public class InventoryPurchaser {
    private final EventPublisher eventPublisher;

    @Transactional
    public Inventory purchaseNewInventory(PurchaseNewInventoryCommand command) {
        // 재고 저장
        Inventory savedInventory = inventoryPersistentRepository.addInventory(newInventory);

        // 이벤트 발행 (자동)
        InventoryPurchasedEvent event = InventoryPurchasedEvent.builder()
            .inventoryId(savedInventory.getInventoryId().getValue())
            .itemName(savedInventory.getItemName())
            .quantity(savedInventory.getQuantity().value())
            .occurredOn(LocalDateTime.now())
            .build();

        eventPublisher.publish(event);
        return savedInventory;
    }
}
```

### 이벤트 구독 (Consumer 예시)
```java
@Component
public class InventoryEventConsumer {

    @KafkaListener(topics = "inventory-events", groupId = "inventory-consumer-group")
    public void consumeInventoryEvent(String message) {
        // 이벤트 처리 로직
        log.info("Received inventory event: {}", message);
    }
}
```

## 테스트

### 단위 테스트
Mock을 사용한 이벤트 발행 검증:
- `InventoryPurchaserWithEventTest`
- `UseInventoryWithEventTest`

### 통합 테스트
Embedded Kafka를 사용한 통합 테스트:
- `KafkaEventPublisherTest`

```bash
# 테스트 실행
./gradlew test --tests "*Event*"
```

## 모니터링

### Kafka 토픽 메시지 확인
```bash
# Kafka 컨슈머 콘솔로 메시지 확인
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic inventory-events \
  --from-beginning
```

### 로그
이벤트 발행 시 다음과 같은 로그가 출력됩니다:
```
INFO  Publishing event to Kafka: type=InventoryPurchased, topic=inventory-events
INFO  Successfully published event: type=InventoryPurchased, offset=0, partition=0
```

## 트러블슈팅

### Kafka 연결 실패
```
Error: Failed to publish event to Kafka
```
**해결방법:**
1. Kafka 서버가 실행 중인지 확인
2. `application.yml`의 `bootstrap-servers` 설정 확인
3. 네트워크 방화벽 설정 확인

### JSON 직렬화 오류
```
Error: Could not serialize object
```
**해결방법:**
1. 이벤트 클래스에 `@JsonProperty` 어노테이션 확인
2. Jackson 의존성 확인
3. `LocalDateTime` 직렬화를 위한 `JavaTimeModule` 등록 확인

## 참고 자료
- [Spring Kafka Documentation](https://spring.io/projects/spring-kafka)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
