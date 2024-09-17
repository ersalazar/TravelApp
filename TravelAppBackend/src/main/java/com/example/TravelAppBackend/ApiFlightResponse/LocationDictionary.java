package com.example.TravelAppBackend.ApiFlightResponse;

// LocationDictionary.java
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDictionary {
    private String cityCode;
    private String countryCode;
}

