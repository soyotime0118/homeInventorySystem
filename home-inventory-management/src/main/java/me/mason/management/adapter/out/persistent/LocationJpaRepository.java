package me.mason.management.adapter.out.persistent;

import me.mason.management.adapter.out.persistent.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationJpaRepository extends JpaRepository<LocationEntity, Long> {
}
