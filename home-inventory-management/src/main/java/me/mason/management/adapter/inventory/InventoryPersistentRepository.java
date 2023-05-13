package me.mason.management.adapter.inventory;

import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import me.mason.management.ports.out.ModifyInventoryStateOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryPersistentRepository implements ModifyInventoryStateOutput {

    private final InventoryJpaRepository inventoryJpaRepository;
    private final InventoryMapper inventoryMapper;

    @Autowired
    public InventoryPersistentRepository(
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
}
