package com.example.TravelAppBackend.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightSegment {
    private String departureDate;
    private String arrivalDate;
    private Airport departureAirport;
    private Airport arrivalAirport;
    private Airline airline;
    private Airline operatingAirline;
    private String flightNumber;
    private String aircraftType;
    private TravelerFareDetails TravelerfareDetails;


}
