package me.mason.management.infrastructure.adapter.out.persistent;

import me.mason.management.infrastructure.adapter.out.persistent.entity.LocationEntity;
import me.mason.management.infrastructure.adapter.out.persistent.repository.LocationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationPersistentAdapter {

    private final LocationJpaRepository locationJpaRepository;

    @Autowired
    public LocationPersistentAdapter(LocationJpaRepository locationJpaRepository) {
        this.locationJpaRepository = locationJpaRepository;
    }

    public LocationEntity registerLocation(String locationName) {
        LocationEntity entity = new LocationEntity();
        entity.setLocationName(locationName);
        return locationJpaRepository.save(entity);
    }

    public List<LocationEntity> findAll() {
        return locationJpaRepository.findAll();
    }

    public LocationEntity getOrCreateByName(String locationName) {
        return locationJpaRepository.findByLocationName(locationName)
                .orElseGet(() -> registerLocation(locationName));
    }
}
