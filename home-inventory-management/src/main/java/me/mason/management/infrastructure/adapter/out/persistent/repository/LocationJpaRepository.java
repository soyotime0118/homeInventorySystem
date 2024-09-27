package me.mason.management.infrastructure.adapter.out.persistent.repository;

import me.mason.management.infrastructure.adapter.out.persistent.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, Long> {
}
