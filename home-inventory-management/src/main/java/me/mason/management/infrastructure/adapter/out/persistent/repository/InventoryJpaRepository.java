package me.mason.management.infrastructure.adapter.out.persistent.repository;

import me.mason.management.infrastructure.adapter.out.persistent.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {
    // 유통기한이 지난 아이템을 가져오기
    List<InventoryEntity> findByExpiryDateBefore(LocalDate expiryDate);

    // 이름으로 아이템을 검색하기 (부분 일치 검색)
    List<InventoryEntity> findByItemNameContaining(String name);

    // 재고가 특정 수치 이하인 아이템을 가져오기
    List<InventoryEntity> findByQuantityLessThanEqual(int quantity);

    Optional<InventoryEntity> findById(Long id);

//    List<InventoryEntity> findByCategory(Category category);
//
//    List<InventoryEntity> findByStorageLocation(StorageLocation location);

//    void delete(Long id);
}
