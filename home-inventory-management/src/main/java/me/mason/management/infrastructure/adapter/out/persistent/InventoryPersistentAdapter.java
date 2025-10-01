package me.mason.management.infrastructure.adapter.out.persistent;

import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.model.Location.LocationId;
import me.mason.management.infrastructure.adapter.out.persistent.entity.InventoryEntity;
import me.mason.management.infrastructure.adapter.out.persistent.mapper.InventoryMapper;
import me.mason.management.infrastructure.adapter.out.persistent.repository.InventoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public Inventory addInventory(Inventory createdInventory) {
        InventoryEntity entity = inventoryMapper.mapToJpaEntity(createdInventory);
        InventoryEntity savedEntity = inventoryJpaRepository.save(entity);
        return inventoryMapper.mapToDomain(savedEntity);
    }

    @Override
    public Inventory updateInventory(Inventory inventory) {
        InventoryEntity inventoryEntity = inventoryMapper.mapToJpaEntity(inventory);
        return inventoryMapper.mapToDomain(inventoryJpaRepository.save(inventoryEntity));
    }

    @Override
    public void deleteInventory(InventoryId inventoryId) {
        inventoryJpaRepository.deleteById(inventoryId.getValue());
    }

    @Override
    public boolean existsById(InventoryId inventoryId) {
        return inventoryJpaRepository.existsById(inventoryId.getValue());
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

    @Override
    public List<Inventory> loadByLocationId(LocationId locationId) {
        return inventoryJpaRepository.findByLocationId(locationId.value()).stream()
                .map(inventoryMapper::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Inventory> updateInventoryLocation(InventoryId inventoryId, LocationId locationId) {
        return inventoryJpaRepository.findById(inventoryId.getValue()).map(entity -> {
            entity.setLocationId(locationId.value());
            return inventoryMapper.mapToDomain(inventoryJpaRepository.save(entity));
        });
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryJpaRepository.findAll().stream()
                .map(inventoryMapper::mapToDomain)
                .collect(Collectors.toList());
    }
}
