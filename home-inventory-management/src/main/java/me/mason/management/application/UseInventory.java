package me.mason.management.application;

import me.mason.management.config.RedissonTransactional;
import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.in.NotFindStockException;
import me.mason.management.ports.in.UseInventoryInput;
import me.mason.management.ports.out.ModifyInventoryStateOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
     * @return
     */
    @Override
    @RedissonTransactional(keyName = "orderProduct:")
    public int usingInventory(InventoryId inventoryId, Integer quantity) {
        //id기준으로 Inventory 가져오기
        Inventory inventory = inventoryPersistentRepository.loadByInventoryId(inventoryId)
                .orElseThrow(() -> new NotFindStockException(inventoryId));

        //재고 사용처리
        inventory.use(quantity);

        inventoryPersistentRepository.modifyInventory(inventory);

        return inventory.getQuantity();
    }
}
