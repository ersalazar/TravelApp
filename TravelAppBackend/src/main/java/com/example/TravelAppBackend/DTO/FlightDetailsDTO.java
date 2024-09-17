package com.example.TravelAppBackend.DTO;



import com.example.TravelAppBackend.ApiFlightResponse.PriceBreakdown;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightDetailsDTO {

    private List<FlightDTO> flights;
    private PriceBreakdown priceBreakdown;


}
