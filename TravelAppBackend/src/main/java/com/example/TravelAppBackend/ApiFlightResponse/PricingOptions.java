package com.example.TravelAppBackend.ApiFlightResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PricingOptions {
    private List<String> fareType;
    private boolean includedCheckedBagsOnly;
}
