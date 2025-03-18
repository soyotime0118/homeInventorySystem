package me.mason.management.application.ports.in;

import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.application.ports.in.dto.RegisterInventoryCommand;
import me.mason.management.application.ports.in.dto.UpdateInventoryCommand;

public interface ManageInventoryInput {

    /**
     * 새로운 물품을 등록합니다.
     *
     * @param command 물품 등록 정보를 담은 커맨드 객체
     * @return 생성된 Inventory 객체
     * @throws IllegalArgumentException 유효하지 않은 입력값이 있을 경우
     */
    Inventory registerInventory(RegisterInventoryCommand command);

    /**
     * 등록된 물품 정보를 수정합니다.
     *
     * @param command 물품 수정 정보를 담은 커맨드 객체
     * @return 수정된 Inventory 객체
     * @throws IllegalArgumentException 유효하지 않은 입력값이 있을 경우
     * @throws IllegalStateException 해당 ID의 물품이 존재하지 않을 경우
     */
    Inventory updateInventory(UpdateInventoryCommand command);

    /**
     * 등록된 물품을 삭제합니다.
     *
     * @param inventoryId 삭제할 물품의 ID
     * @throws IllegalStateException 해당 ID의 물품이 존재하지 않을 경우
     */
    void deleteInventory(InventoryId inventoryId);
}