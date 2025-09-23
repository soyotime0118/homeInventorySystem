package me.mason.management.domain.model;

import me.mason.management.application.ports.in.dto.RegisterInventoryCommand;
import me.mason.management.domain.model.Inventory.InventoryBuilder;
import me.mason.management.domain.model.Inventory.InventoryId;
import me.mason.management.domain.model.Inventory.Quantity;

import java.time.LocalDateTime;
import java.util.Objects;

public class InventoryFactory {

    /**
     * RegisterInventoryCommand를 사용하여 Inventory 객체를 생성합니다.
     *
     * @param command 물품 등록 정보를 담은 커맨드 객체
     * @return 생성된 Inventory 객체
     */
    public static Inventory createInventory(RegisterInventoryCommand command) {
        // 만료일을 기본값으로 설정 (예: 현재 날짜로부터 1년 후)
        LocalDateTime defaultExpiryDate = LocalDateTime.now().plusYears(1);

        return withoutId(
                command.itemName(),
                command.quantity(),
                command.locationId(),
                command.brand(),
                command.purchaseLocation(),
                LocalDateTime.now(), // 마지막 구매 일시를 현재 시간으로 설정
                defaultExpiryDate
        ).build();
    }


    private static InventoryBuilder defaultBuilder(
            InventoryId inventoryId,
            String itemName,
            Quantity quantity,
            Long locationId,
            String brand,
            String purchaseLocation,
            LocalDateTime lastPurchase,
            LocalDateTime lastUsedDate,
            LocalDateTime expiryDate

    ) {
        InventoryBuilder builder = Inventory.builder()
                .itemName(itemName)
                .quantity(quantity)
                .locationId(locationId)
                .brand(brand)
                .purchaseLocation(purchaseLocation)
                .lastUsedDate(lastUsedDate)
                .lastPurchase(lastPurchase)
                .expiryDate(expiryDate);
        if (Objects.nonNull(inventoryId)) {
            builder.inventoryId(inventoryId);
        }
        return builder;
    }

    /**
     * {@link InventoryId }가 존재하는 경우 사용하는 Builder, 주로 저장소에서 객체를 로드할 때 호출
     *
     * @param inventoryId      저장된 InventoryId
     * @param itemName         인벤토리 이름
     * @param quantity         수량
     * @param locationId       위치 ID
     * @param brandName        브랜드 이름
     * @param purchaseLocation 구매 위치
     * @param lastPurchase     마지막 구매 일시
     * @return 새로운 Inventory 객체
     */
    public static Inventory withId(
            InventoryId inventoryId,
            String itemName,
            long quantity,
            Long locationId,
            String brandName,
            String purchaseLocation,
            LocalDateTime lastPurchase,
            LocalDateTime lastUsedDate,
            LocalDateTime expiryDate
    ) {
        return defaultBuilder(
                inventoryId,
                itemName,
                Quantity.of(quantity),
                locationId,
                brandName,
                purchaseLocation,
                lastPurchase,
                lastUsedDate,
                expiryDate)
                .build();
    }

    /**
     * {@link InventoryId }가 존재하지 않는 경우 사용하는 Builder, 주로 새로운 객체를 생성할 때 호출
     *
     * @param itemName         인벤토리 이름
     * @param quantity         수량
     * @param locationId       위치 ID
     * @param brandName        브랜드 이름
     * @param purchaseLocation 구매 위치
     * @param lastPurchase     마지막 구매 일시
     * @return 새로운 Inventory 객체
     * @throws IllegalArgumentException quantity가 음수일 경우 발생
     */
    static InventoryBuilder withoutId(
            String itemName,
            long quantity,
            long locationId,
            String brandName,
            String purchaseLocation,
            LocalDateTime lastPurchase,
            LocalDateTime expiryDate
    ) {
        if (expiryDate.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("만료일은 현재 날짜보다 이후여야 합니다.");
        }
        return defaultBuilder(
                null,
                itemName,
                Quantity.of(quantity),
                locationId,
                brandName,
                purchaseLocation,
                lastPurchase,
                expiryDate
        );
    }
}