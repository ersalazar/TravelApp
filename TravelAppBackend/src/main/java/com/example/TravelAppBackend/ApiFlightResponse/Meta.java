package com.example.TravelAppBackend.ApiFlightResponse;

import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {
    private int count;
    private Links links;
}
