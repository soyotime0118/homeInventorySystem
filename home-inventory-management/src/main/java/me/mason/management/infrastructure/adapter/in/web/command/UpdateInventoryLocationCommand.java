package me.mason.management.infrastructure.adapter.in.web.command;

public record UpdateInventoryLocationCommand(
        Long inventoryId,
        Long locationId
) {}
