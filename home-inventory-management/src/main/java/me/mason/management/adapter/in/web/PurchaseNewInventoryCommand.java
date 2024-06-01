package me.mason.management.adapter.in.web;

import lombok.Getter;

@Getter
public class PurchaseNewInventoryCommand {
    private final String name;
    private final int quantity;
    private final String location;

    public PurchaseNewInventoryCommand(
            String name,
            int quantity,
            String location) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        this.name = name;
        this.quantity = quantity;
        this.location = location;
    }
}
