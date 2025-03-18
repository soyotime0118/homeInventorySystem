package me.mason.management.infrastructure.adapter.out.persistent.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class InventoryEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;

    private long quantity;

    private Long locationId;

    private String brand;

    private String purchaseLocation;

    private LocalDateTime purchaseDate;

    private LocalDateTime expiryDate;
}
