package com.example.demo.repository;

import com.example.demo.entity.Location;
import com.example.demo.entity.WeatherData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RepositoryRestResource(collectionResourceRel = "weatherdata", path = "weatherdata")
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    // Zmienione: zamiast locationId użyj location.id
    @RestResource(path = "byDateAndLocation", rel = "byDateAndLocation")
    Page<WeatherData> findByDateGreaterThanEqualAndLocation_Id(
            @Param("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @Param("locationId") Long locationId,
            Pageable pageable);

    // Poprawione: użyj _ zamiast camelCase dla zagnieżdżonych właściwości
    @RestResource(path = "byTemperatureGreaterThan", rel = "byTemperatureGreaterThan")
    Page<WeatherData> findByTemperatureGreaterThan(Double temperature, Pageable pageable);

    // Poprawione: użyj __ dla zagnieżdżonych właściwości (Spring Data JPA 4.x)
    @RestResource(path = "byLocationCountryCode", rel = "byLocationCountryCode")
    Page<WeatherData> findByLocation_CountryCode(String countryCode, Pageable pageable);

    // Dodatkowe metody
    @RestResource(path = "byLocationCity", rel = "byLocationCity")
    Page<WeatherData> findByLocation_CityName(String cityName, Pageable pageable);

    @RestResource(path = "byDateBetween", rel = "byDateBetween")
    Page<WeatherData> findByDateBetween(
            @Param("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Param("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            Pageable pageable);
}