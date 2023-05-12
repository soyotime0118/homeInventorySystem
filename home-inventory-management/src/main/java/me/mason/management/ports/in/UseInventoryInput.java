package me.mason.management.ports.in;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public interface UseInventoryInput {

    /**
     * @param usingInventoryRequestDto 재고사용요청
     * @return 재고 사용후 재고상태
     */
    UsingInventoryResponseDto usingInventory(UsingInventoryRequestDto usingInventoryRequestDto);

    @Getter
    @Setter
    class UsingInventoryRequestDto {

        private Long inventoryCode;
        //todo 사용수량 변경필요
        private int useQuantity;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    class UsingInventoryResponseDto {
        private Long inventoryCode;
        private int quantity;
    }
}
