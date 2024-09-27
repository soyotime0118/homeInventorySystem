package me.mason.management.infrastructure.adapter.out.persistent.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "storage_locations")
public class LocationEntity {

    @Id
    @Column(name = "locationId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String locationName;
}
