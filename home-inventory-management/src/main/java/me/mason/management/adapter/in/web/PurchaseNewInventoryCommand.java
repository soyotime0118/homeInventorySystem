package me.mason.management.adapter.in.web;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PurchaseNewInventoryCommand {
    private final String name;
    private final int quantity;
    private final String location;
}
