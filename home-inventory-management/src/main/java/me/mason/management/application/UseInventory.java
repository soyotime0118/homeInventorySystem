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
     * @param usingInventoryRequestDto 재고사용요청Dto 재고ID, 사용량을 담고 있다
     * @return 현재 재고량
     */
    @Override
    @Transactional
    public UsingInventoryResponseDto usingInventory(UsingInventoryRequestDto usingInventoryRequestDto) {
        //현재 재고량 가져 오기
        Inventory inventory = modifyInventoryStateOutput.loadByInventoryId(
                new InventoryId(usingInventoryRequestDto.getInventoryCode()));

        //재고 사용처리
        inventory.use(usingInventoryRequestDto.getUseQuantity());

        modifyInventoryStateOutput.modifyInventory(inventory);

        return new UsingInventoryResponseDto(inventory.getInventoryId().getValue(), inventory.getQuantity());
    }
}
