package me.mason.management.domain.service;

import me.mason.management.application.ports.in.ManageLocationInput;
import me.mason.management.application.ports.out.LocationOutput;
import me.mason.management.domain.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationManager implements ManageLocationInput {

    private final LocationOutput locationOutput;

    @Autowired
    public LocationManager(LocationOutput locationOutput) {
        this.locationOutput = locationOutput;
    }

    @Override
    @Transactional
    public Location registerLocation(String name) {
        return locationOutput.registerLocation(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> listLocations() {
        return locationOutput.findAll();
    }
}
