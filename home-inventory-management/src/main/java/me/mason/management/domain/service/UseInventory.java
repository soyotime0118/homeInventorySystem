package me.mason.management.domain.service;

import lombok.extern.slf4j.Slf4j;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.exception.NotFindStockException;
import me.mason.management.application.ports.in.UseInventoryInput;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.model.Inventory.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class UseInventory implements UseInventoryInput {

    private final ModifyInventoryStateOutput inventoryPersistentRepository;


    @Autowired
    public UseInventory(ModifyInventoryStateOutput inventoryPersistentRepository) {
        this.inventoryPersistentRepository = inventoryPersistentRepository;
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

        return inventory.getQuantity().value();
    }
}
