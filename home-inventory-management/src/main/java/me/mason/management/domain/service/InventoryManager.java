package me.mason.management.domain.service;

import lombok.extern.slf4j.Slf4j;
import me.mason.management.application.ports.in.ManageInventoryInput;
import me.mason.management.application.ports.in.dto.RegisterInventoryCommand;
import me.mason.management.application.ports.in.dto.UpdateInventoryCommand;
import me.mason.management.application.ports.out.LocationOutput;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.exception.LocationDoesNotExistException;
import me.mason.management.domain.exception.NotFindStockException;
import me.mason.management.domain.model.InventoryFactory;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.model.Location.LocationId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class InventoryManager implements ManageInventoryInput {

    private final ModifyInventoryStateOutput inventoryPersistentRepository;

    private final LocationOutput locationOutput;

    @Autowired
    public InventoryManager(ModifyInventoryStateOutput inventoryPersistentRepository, LocationOutput locationOutput) {
        this.inventoryPersistentRepository = inventoryPersistentRepository;
        this.locationOutput = locationOutput;
    }

    /**
     * 주어진 등록 명령을 기반으로 새로운 인벤토리를 생성하고 저장소에 추가합니다.
     *
     * @param command 인벤토리 등록 정보를 포함한 명령 객체
     * @return 생성된 인벤토리 객체
     */
    @Override
    @Transactional
    public Inventory registerInventory(RegisterInventoryCommand command) {

        Inventory createdInventory = InventoryFactory.createInventory(command);

        boolean isLocationExists = locationOutput.existsById(createdInventory.getLocationId());
        if (!isLocationExists) {
            throw new LocationDoesNotExistException();
        }

        return inventoryPersistentRepository.addInventory(createdInventory);
    }


    /**
     * 주어진 업데이트 명령을 기반으로 인벤토리 정보를 갱신합니다.
     *
     * @param command 인벤토리 업데이트 정보를 포함한 명령 객체
     * @return 갱신된 인벤토리 객체
     */
    @Override
    @Transactional
    public Inventory updateInventory(UpdateInventoryCommand command) {

        Inventory inventory = inventoryPersistentRepository.loadByInventoryId(
                        InventoryId.of(command.inventoryId()))
                .orElseThrow();
        inventory.changeDetail(
                command.itemName(),
                command.brand(),
                command.purchaseLocation(),
                new LocationId(command.locationId()),
                command.purchaseDate(),
                command.expiryDate());
        return inventoryPersistentRepository.updateInventory(inventory);
    }

    /**
     * 주어진 인벤토리 ID를 기반으로 인벤토리를 삭제합니다.
     *
     * @param inventoryId 삭제할 인벤토리의 ID
     */
    @Override
    public void deleteInventory(InventoryId inventoryId) {
        if (inventoryId == null || inventoryId.getValue() == null) {
            throw new IllegalArgumentException("인벤토리 ID는 null일 수 없습니다");
        }

        boolean exists = inventoryPersistentRepository.existsById(inventoryId);
        if (!exists) {
            throw new NotFindStockException(inventoryId);
        }
        inventoryPersistentRepository.deleteInventory(inventoryId);
    }
}