package me.mason.management.adapter.out.persistent;

import me.mason.management.adapter.out.persistent.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {
}
