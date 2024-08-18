package me.mason.management.adapter.out.persistent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationPersistentRepository {

    private final LocationJpaRepository locationJpaRepository;


    @Autowired
    public LocationPersistentRepository(LocationJpaRepository locationJpaRepository) {
        this.locationJpaRepository = locationJpaRepository;
    }
}
