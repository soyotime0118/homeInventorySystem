package me.mason.management.adapter.inventory.web;

import me.mason.management.ports.in.UseInventoryInput;
import me.mason.management.ports.in.UseInventoryInput.UsingInventoryRequestDto;
import me.mason.management.ports.in.UseInventoryInput.UsingInventoryResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("inventory")
@RestController
public class InventoryRestController {

    private final UseInventoryInput useInventoryInput;

    @Autowired
    public InventoryRestController(UseInventoryInput useInventoryInput) {
        this.useInventoryInput = useInventoryInput;
    }


    @PutMapping("use")
    public UsingInventoryResponseDto useInventory(@RequestBody UsingInventoryRequestDto requestDto) {
        return useInventoryInput.usingInventory(requestDto);
    }
}
