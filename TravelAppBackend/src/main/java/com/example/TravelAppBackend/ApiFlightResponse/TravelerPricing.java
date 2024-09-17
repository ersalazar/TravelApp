package com.example.TravelAppBackend.ApiFlightResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TravelerPricing {
    private String travelerId;
    private String fareOption;
    private String travelerType;
    private TravelerPrice price;
    private List<FareDetailsBySegment> fareDetailsBySegment;
}
