package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "weather_data")
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    private Double temperature;
    private Double humidity;
    private Double pressure;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    @JsonIgnore  // Ignorujemy w serializacji JSON
    private Location location;

    @Transient  // Pole tymczasowe tylko dla deserializacji
    private String locationUrl;

    public WeatherData() {}

    public WeatherData(LocalDate date, Double temperature, Location location) {
        this.date = date;
        this.temperature = temperature;
        this.location = location;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public Double getHumidity() { return humidity; }
    public void setHumidity(Double humidity) { this.humidity = humidity; }

    public Double getPressure() { return pressure; }
    public void setPressure(Double pressure) { this.pressure = pressure; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @JsonIgnore
    public Location getLocation() { return location; }

    public void setLocation(Location location) {
        this.location = location;
        if (location != null && location.getId() != null) {
            this.locationUrl = "/api/locations/" + location.getId();
        }
    }

    // Custom getter dla serializacji - zwraca link do lokalizacji
    @JsonProperty("location")
    public String getLocationLink() {
        if (locationUrl != null) {
            return locationUrl;
        }
        return location != null ? "/api/locations/" + location.getId() : null;
    }

    // Custom setter dla deserializacji - przetwarza link na obiekt Location
    @JsonProperty("location")
    public void setLocationFromLink(String locationLink) {
        this.locationUrl = locationLink;
        if (locationLink != null) {
            try {
                // Ekstrakcja ID z URL
                String idStr = locationLink.substring(locationLink.lastIndexOf("/") + 1);
                Long locationId = Long.parseLong(idStr);

                // Tworzymy tymczasowy obiekt Location z ID
                Location loc = new Location();
                loc.setId(locationId);
                this.location = loc;
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid location link: " + locationLink);
            }
        }
    }

    // Getter dla ID lokalizacji (dla query methods)
    @JsonProperty("locationId")
    public Long getLocationId() {
        return location != null ? location.getId() : null;
    }

    // Setter dla ID lokalizacji (alternatywna metoda)
    @JsonProperty("locationId")
    public void setLocationId(Long locationId) {
        if (locationId != null) {
            Location loc = new Location();
            loc.setId(locationId);
            this.location = loc;
            this.locationUrl = "/api/locations/" + locationId;
        }
    }

    // JPA callback - upewniamy się, że location jest ustawione przed zapisem
    @PrePersist
    @PreUpdate
    private void validateLocation() {
        if (location == null || location.getId() == null) {
            throw new IllegalStateException("Location must be set with a valid ID before saving");
        }
    }
}