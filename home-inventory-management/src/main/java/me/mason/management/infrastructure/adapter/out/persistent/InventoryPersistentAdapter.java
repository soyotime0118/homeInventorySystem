package me.mason.management.infrastructure.adapter.out.persistent;

import me.mason.management.infrastructure.adapter.out.persistent.mapper.InventoryMapper;
import me.mason.management.infrastructure.adapter.out.persistent.entity.InventoryEntity;
import me.mason.management.infrastructure.adapter.out.persistent.repository.InventoryJpaRepository;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryPersistentAdapter implements ModifyInventoryStateOutput {

    private final InventoryJpaRepository inventoryJpaRepository;
    private final InventoryMapper inventoryMapper;

    @Autowired
    public InventoryPersistentAdapter(
            InventoryJpaRepository inventoryJpaRepository,
            InventoryMapper inventoryMapper) {
        this.inventoryJpaRepository = inventoryJpaRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    public void modifyInventory(Inventory inventory) {

        InventoryEntity inventoryEntity = inventoryMapper.mapToJpaEntity(inventory);

        inventoryJpaRepository.save(inventoryEntity);
    }

    @Override
    public Optional<Inventory> loadByInventoryId(InventoryId inventoryId) {
        return inventoryJpaRepository.findById(inventoryId.getValue())
                .map(inventoryMapper::mapToDomain);
    }

    @Override
    public InventoryId addInventory(String inventoryName, int inventoryQuantity, int locationId) {
        //todo 여기서도 파라미터 체크를 해야할까?
        InventoryEntity inventoryEntity = inventoryMapper.mapToJpaEntity(
                Inventory.withoutId(
                        inventoryName,
                        inventoryQuantity,
                        locationId,
                        LocalDateTime.now()));
        //todo JpaRepository 가 파라미터 객체의 ID를 수정하는지 확인 필요,
        // 만약 수정하지 않는다면, Entity를 만들어서 넘기지 말고, 각각 파라미터를 직접 전달하고
        // 결과값으로 InventoryEntity를 리턴해야함
        inventoryJpaRepository.save(inventoryEntity);
        return InventoryId.of(inventoryEntity.getId());

    }

    public List<Inventory> loadByContainingInventoryName(String inventoryName) {
        return inventoryJpaRepository.findByItemNameContaining(inventoryName).stream()
                .map(inventoryMapper::mapToDomain)
                .collect(Collectors.toList());
    }

    public List<Inventory> loadByExpiryDateBefore(LocalDate expiryDate) {
        return inventoryJpaRepository.findByExpiryDateBefore(expiryDate).stream()
                .map(inventoryMapper::mapToDomain)
                .collect(Collectors.toList());
    }

    public List<Inventory> loadByExpiryDateBefore(int quantity) {
        return inventoryJpaRepository.findByQuantityLessThanEqual(quantity).stream()
                .map(inventoryMapper::mapToDomain)
                .collect(Collectors.toList());
    }
}
