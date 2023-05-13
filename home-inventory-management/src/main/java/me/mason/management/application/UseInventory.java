package me.mason.management.application;

import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.in.UseInventoryInput;
import me.mason.management.ports.out.ModifyInventoryStateOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UseInventory implements UseInventoryInput {

    private final ModifyInventoryStateOutput modifyInventoryStateOutput;


    @Autowired
    public UseInventory(ModifyInventoryStateOutput modifyInventoryStateOutput) {
        this.modifyInventoryStateOutput = modifyInventoryStateOutput;
    }

    /**
     * 재고 사용 처리
     *
     * @return 현재 재고량
     */
    @Override
    @Transactional
    public int usingInventory(InventoryId inventoryId, Integer quantity) {
        //현재 재고량 가져 오기
        Inventory inventory = modifyInventoryStateOutput.loadByInventoryId(inventoryId);

        //재고 사용처리
        inventory.use(quantity);

        modifyInventoryStateOutput.modifyInventory(inventory);

        return inventory.getQuantity();
    }
}
