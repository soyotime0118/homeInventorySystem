package me.mason.management.domain.service;

import me.mason.management.application.ports.out.EventPublisher;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.event.InventoryUsedEvent;
import me.mason.management.domain.model.Inventory;
import me.mason.management.domain.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * UseInventory 이벤트 발행 테스트
 */
class UseInventoryWithEventTest {

    @Mock
    private ModifyInventoryStateOutput inventoryPersistentRepository;

    @Mock
    private EventPublisher eventPublisher;

    private UseInventory useInventory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useInventory = new UseInventory(inventoryPersistentRepository, eventPublisher);
    }

    @Test
    @DisplayName("재고 사용 시 InventoryUsedEvent 발행")
    void shouldPublishInventoryUsedEventWhenUsingInventory() {
        // given
        Inventory.InventoryId inventoryId = Inventory.InventoryId.of(1L);
        Inventory.Quantity useQuantity = Inventory.Quantity.of(3L);

        Inventory inventory = Inventory.builder()
            .inventoryId(inventoryId)
            .itemName("Test Item")
            .quantity(Inventory.Quantity.of(10L))
            .locationId(new Location.LocationId(1L))
            .build();

        when(inventoryPersistentRepository.loadByInventoryId(inventoryId))
            .thenReturn(Optional.of(inventory));

        // when
        long remainingQuantity = useInventory.usingInventory(inventoryId, useQuantity);

        // then
        assertEquals(7L, remainingQuantity);

        ArgumentCaptor<InventoryUsedEvent> eventCaptor =
            ArgumentCaptor.forClass(InventoryUsedEvent.class);
        verify(eventPublisher, times(1)).publish(eventCaptor.capture());

        InventoryUsedEvent publishedEvent = eventCaptor.getValue();
        assertEquals(1L, publishedEvent.getInventoryId());
        assertEquals("Test Item", publishedEvent.getItemName());
        assertEquals(3L, publishedEvent.getUsedQuantity());
        assertEquals(7L, publishedEvent.getRemainingQuantity());
        assertNotNull(publishedEvent.getOccurredOn());
    }

    @Test
    @DisplayName("재고 사용 및 저장소 업데이트 호출 확인")
    void shouldCallRepositoryWhenUsingInventory() {
        // given
        Inventory.InventoryId inventoryId = Inventory.InventoryId.of(1L);
        Inventory.Quantity useQuantity = Inventory.Quantity.of(2L);

        Inventory inventory = Inventory.builder()
            .inventoryId(inventoryId)
            .itemName("Test Item")
            .quantity(Inventory.Quantity.of(5L))
            .locationId(new Location.LocationId(1L))
            .build();

        when(inventoryPersistentRepository.loadByInventoryId(inventoryId))
            .thenReturn(Optional.of(inventory));

        // when
        useInventory.usingInventory(inventoryId, useQuantity);

        // then
        verify(inventoryPersistentRepository, times(1))
            .loadByInventoryId(inventoryId);
        verify(inventoryPersistentRepository, times(1))
            .modifyInventory(any(Inventory.class));
        verify(eventPublisher, times(1))
            .publish(any(InventoryUsedEvent.class));
    }
}
