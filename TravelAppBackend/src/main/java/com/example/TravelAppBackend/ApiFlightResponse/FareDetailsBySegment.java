package com.example.TravelAppBackend.ApiFlightResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FareDetailsBySegment {
    private String segmentId;
    private String cabin;
    private String fareBasis;
    private String brandedFare;
    private String brandedFareLabel;
    @JsonProperty("class")
    private String flightClass;
    private IncludedCheckedBags includedCheckedBags;
    private List<Amenity> amenities;
}
