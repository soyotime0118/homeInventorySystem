package me.mason.management.adapter.out.persistent;

import me.mason.management.adapter.out.persistent.entity.InventoryEntity;
import me.mason.management.domain.Inventory;
import me.mason.management.domain.Inventory.InventoryId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryPersistentRepositoryTest {

    @InjectMocks
    InventoryPersistentRepository inventoryPersistentRepository;

    /**
     * Mock 객체 사용
     * InventoryJpaRepository 는 테스트 대상이 아니면서 라이브러리로 Mock 객체를 썼다.
     * 생성하기 위해서는 Spring-Data-Jpa의 의존성이 필요하다.
     * InventoryJpaRepository에서 어떤 메소드를 호출할지 알고 있고, 어떤값을 리턴해야 하는지도 알고 있다.
     * 이 코드의 역할은 DB 테이블을 업데이트 하는 것이지만, 이 테스트코드는 DB의존성이 없는 테스트 코드이기 때문에
     * 실제 DB 테이블이 업데이트 되었는지까지는 확인할 필요가 없고, DB 테이블의 추상화를 통해 확인하면 된다.
     */
    @Mock
    InventoryJpaRepository inventoryJpaRepository;

    /**
     * SPY 객체 사용
     * Inventory
     */
    @Spy
    InventoryMapper inventoryMapper = new InventoryMapper();

    @Mock
    InventoryEntity mockInventoryEntity;

    @DisplayName("신규재고 추가시 성공할경우 추가된 재고ID를 리턴")
    @Test
    void addInventory() {
        when(inventoryJpaRepository.save(any(InventoryEntity.class))).thenReturn(mockInventoryEntity);

        InventoryId inventoryId = inventoryPersistentRepository.addInventory("name", 1, "basement");


        Assertions.assertThat(inventoryId).isNotNull();
    }
}