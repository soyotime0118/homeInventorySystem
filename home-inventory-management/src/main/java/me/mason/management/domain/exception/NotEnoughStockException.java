package me.mason.management.domain.exception;

public class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException(long quantity) {
        super(String.format("재고가 부족합니다. 부족수량 %d ", quantity));
    }
}
