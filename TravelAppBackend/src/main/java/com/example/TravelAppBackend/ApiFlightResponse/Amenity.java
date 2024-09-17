package com.example.TravelAppBackend.ApiFlightResponse;

// Amenity.java
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Amenity {
    private String description;
    private boolean isChargeable;
    private String amenityType;
    private AmenityProvider amenityProvider;
}
