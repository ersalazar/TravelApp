package com.example.TravelAppBackend.ApiFlightResponse;

// Fee.java
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fee {
    private String amount;
    private String type;
}
