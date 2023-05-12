package me.mason.management.ports.in;

import me.mason.management.ports.in.UseInventoryInput.UsingInventoryRequestDto;
import me.mason.management.ports.in.UseInventoryInput.UsingInventoryResponseDto;
import org.junit.jupiter.api.Test;

class UseInventoryInputTest {


    UseInventoryInput useInventoryInput;

    @Test
    void name() {

        UsingInventoryRequestDto usingInventoryRequestDto = new UsingInventoryRequestDto();
        usingInventoryRequestDto.setInventoryCode(1000L);
        usingInventoryRequestDto.setUseQuantity(1);
        UsingInventoryResponseDto inventoryResponseDto = useInventoryInput.usingInventory(usingInventoryRequestDto);

    }
}