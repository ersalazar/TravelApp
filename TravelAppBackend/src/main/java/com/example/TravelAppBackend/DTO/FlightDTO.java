package com.example.TravelAppBackend.DTO;

import com.example.TravelAppBackend.model.Airline;
import com.example.TravelAppBackend.model.Airport;
import com.example.TravelAppBackend.model.FlightSegment;
import com.example.TravelAppBackend.model.TravelerFareDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDTO {
    private String departureDateTime;
    private String arrivalDateTime;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private Airline carrier;
    private String totalFlightTime;
    private List<FlightSegment> segments;


}
