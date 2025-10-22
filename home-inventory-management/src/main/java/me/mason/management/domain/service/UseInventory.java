package me.mason.management.domain.service;

import lombok.extern.slf4j.Slf4j;
import me.mason.management.application.ports.out.EventPublisher;
import me.mason.management.domain.event.InventoryUsedEvent;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.exception.NotFindStockException;
import me.mason.management.application.ports.in.UseInventoryInput;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.model.Inventory.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
public class UseInventory implements UseInventoryInput {

    private final ModifyInventoryStateOutput inventoryPersistentRepository;
    private final EventPublisher eventPublisher;


    @Autowired
    public UseInventory(
        ModifyInventoryStateOutput inventoryPersistentRepository,
        EventPublisher eventPublisher
    ) {
        this.inventoryPersistentRepository = inventoryPersistentRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * @param inventoryId 사용처리하는 재고ID
     * @param quantity    수량
     * @return 변경된 재고의 수량
     * @throws IllegalArgumentException 수량이 0이하일 경우
     */
    @Override
    @Transactional
    public long usingInventory(InventoryId inventoryId, Quantity quantity) {
        //id기준으로 Inventory 가져오기
        Inventory inventory = inventoryPersistentRepository.loadByInventoryId(inventoryId)
                .orElseThrow(() -> new NotFindStockException(inventoryId));

        //재고 사용처리
        inventory.use(quantity);
        log.info("InventoryId {} is used, quantity : {}", inventoryId, quantity);

        inventoryPersistentRepository.modifyInventory(inventory);

        // 재고 사용 이벤트 발행
        InventoryUsedEvent event = InventoryUsedEvent.builder()
            .inventoryId(inventory.getInventoryId().getValue())
            .itemName(inventory.getItemName())
            .usedQuantity(quantity.value())
            .remainingQuantity(inventory.getQuantity().value())
            .occurredOn(LocalDateTime.now())
            .build();

        eventPublisher.publish(event);
        log.info("Published InventoryUsedEvent for inventoryId: {}", inventoryId);

        return inventory.getQuantity().value();
    }
}
