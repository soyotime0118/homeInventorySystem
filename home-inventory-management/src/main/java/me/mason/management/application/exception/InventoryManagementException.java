package me.mason.management.application.exception;

import lombok.Getter;

@Getter
public class InventoryManagementException extends RuntimeException {

    private final String message;

    public InventoryManagementException(String message) {
        this.message = message;
    }
}
