package me.mason.management.infrastructure.adapter.out.persistent;

import me.mason.management.infrastructure.adapter.out.persistent.repository.LocationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationPersistentAdapter {

    private final LocationJpaRepository locationJpaRepository;


    @Autowired
    public LocationPersistentAdapter(LocationJpaRepository locationJpaRepository) {
        this.locationJpaRepository = locationJpaRepository;
    }
}
