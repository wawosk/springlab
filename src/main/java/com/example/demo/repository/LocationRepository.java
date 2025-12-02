package com.example.demo.repository;

import com.example.demo.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "locations", path = "locations")
public interface LocationRepository extends JpaRepository<Location, Long> {

    @RestResource(path = "byCountryCode", rel = "byCountryCode")
    Page<Location> findByCountryCode(String countryCode, Pageable pageable);

    @RestResource(path = "byCityName", rel = "byCityName")
    Optional<Location> findByCityName(String cityName);

    @RestResource(path = "byCountryCodeContaining", rel = "byCountryCodeContaining")
    Page<Location> findByCountryCodeContaining(String countryCode, Pageable pageable);
}