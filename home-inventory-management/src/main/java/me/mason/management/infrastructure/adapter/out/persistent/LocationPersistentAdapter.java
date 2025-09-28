package me.mason.management.infrastructure.adapter.out.persistent;

import me.mason.management.application.ports.out.LocationOutput;
import me.mason.management.domain.model.Location;
import me.mason.management.infrastructure.adapter.out.persistent.entity.LocationEntity;
import me.mason.management.infrastructure.adapter.out.persistent.repository.LocationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationPersistentAdapter implements LocationOutput {

    private final LocationJpaRepository locationJpaRepository;

    @Autowired
    public LocationPersistentAdapter(LocationJpaRepository locationJpaRepository) {
        this.locationJpaRepository = locationJpaRepository;
    }

    @Override
    public Location registerLocation(String locationName) {
        LocationEntity entity = new LocationEntity();
        entity.setLocationName(locationName);
        LocationEntity saved = locationJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Location> findAll() {
        return locationJpaRepository.findAll().stream()
                .map(LocationPersistentAdapter::toDomain)
                .toList();
    }

    @Override
    public Optional<Location> getOrCreateByName(String locationName) {
        return locationJpaRepository.findByLocationName(locationName)
                .map(LocationPersistentAdapter::toDomain)
                .or(() -> Optional.of(registerLocation(locationName)));
    }

    private static Location toDomain(LocationEntity entity) {
        return Location.of(entity.getId(), entity.getLocationName());
    }
}
