package me.mason.management.domain.service;

import me.mason.management.application.ports.in.dto.PurchaseNewInventoryCommand;
import me.mason.management.application.ports.out.EventPublisher;
import me.mason.management.application.ports.out.ModifyInventoryStateOutput;
import me.mason.management.domain.event.InventoryPurchasedEvent;
import me.mason.management.domain.event.InventoryStockAddedEvent;
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
 * InventoryPurchaser 이벤트 발행 테스트
 */
class InventoryPurchaserWithEventTest {

    @Mock
    private ModifyInventoryStateOutput inventoryPersistentRepository;

    @Mock
    private EventPublisher eventPublisher;

    private InventoryPurchaser inventoryPurchaser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inventoryPurchaser = new InventoryPurchaser(inventoryPersistentRepository, eventPublisher);
    }

    @Test
    @DisplayName("신규 재고 구매 시 InventoryPurchasedEvent 발행")
    void shouldPublishInventoryPurchasedEventWhenPurchasingNewInventory() {
        // given
        PurchaseNewInventoryCommand command = PurchaseNewInventoryCommand.builder()
            .name("Test Item")
            .quantity(10L)
            .locationId(1)
            .brandName("Test Brand")
            .purchaseLocation("Test Store")
            .build();

        Inventory savedInventory = Inventory.builder()
            .inventoryId(Inventory.InventoryId.of(1L))
            .itemName("Test Item")
            .quantity(Inventory.Quantity.of(10L))
            .locationId(new Location.LocationId(1L))
            .brand("Test Brand")
            .purchaseLocation("Test Store")
            .build();

        when(inventoryPersistentRepository.addInventory(any(Inventory.class)))
            .thenReturn(savedInventory);

        // when
        inventoryPurchaser.purchaseNewInventory(command);

        // then
        ArgumentCaptor<InventoryPurchasedEvent> eventCaptor =
            ArgumentCaptor.forClass(InventoryPurchasedEvent.class);
        verify(eventPublisher, times(1)).publish(eventCaptor.capture());

        InventoryPurchasedEvent publishedEvent = eventCaptor.getValue();
        assertEquals(1L, publishedEvent.getInventoryId());
        assertEquals("Test Item", publishedEvent.getItemName());
        assertEquals(10L, publishedEvent.getQuantity());
        assertEquals(1L, publishedEvent.getLocationId());
        assertEquals("Test Brand", publishedEvent.getBrand());
        assertEquals("Test Store", publishedEvent.getPurchaseLocation());
        assertNotNull(publishedEvent.getOccurredOn());
    }

    @Test
    @DisplayName("기존 재고 추가 구매 시 InventoryStockAddedEvent 발행")
    void shouldPublishInventoryStockAddedEventWhenPurchasingExistingInventory() {
        // given
        Inventory.InventoryId inventoryId = Inventory.InventoryId.of(1L);
        Inventory.Quantity addQuantity = Inventory.Quantity.of(5L);

        Inventory existingInventory = Inventory.builder()
            .inventoryId(inventoryId)
            .itemName("Test Item")
            .quantity(Inventory.Quantity.of(10L))
            .locationId(new Location.LocationId(1L))
            .build();

        Inventory updatedInventory = Inventory.builder()
            .inventoryId(inventoryId)
            .itemName("Test Item")
            .quantity(Inventory.Quantity.of(15L))
            .locationId(new Location.LocationId(1L))
            .build();

        when(inventoryPersistentRepository.loadByInventoryId(inventoryId))
            .thenReturn(Optional.of(existingInventory));
        when(inventoryPersistentRepository.updateInventory(any(Inventory.class)))
            .thenReturn(updatedInventory);

        // when
        inventoryPurchaser.purchaseInventory(inventoryId, addQuantity);

        // then
        ArgumentCaptor<InventoryStockAddedEvent> eventCaptor =
            ArgumentCaptor.forClass(InventoryStockAddedEvent.class);
        verify(eventPublisher, times(1)).publish(eventCaptor.capture());

        InventoryStockAddedEvent publishedEvent = eventCaptor.getValue();
        assertEquals(1L, publishedEvent.getInventoryId());
        assertEquals("Test Item", publishedEvent.getItemName());
        assertEquals(5L, publishedEvent.getAddedQuantity());
        assertEquals(15L, publishedEvent.getTotalQuantity());
        assertNotNull(publishedEvent.getOccurredOn());
    }
}
