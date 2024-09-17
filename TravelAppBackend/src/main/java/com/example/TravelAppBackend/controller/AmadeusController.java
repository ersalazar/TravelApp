package com.example.TravelAppBackend.controller;

import com.example.TravelAppBackend.DTO.*;
import com.example.TravelAppBackend.service.AmadeusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AmadeusController {

    @Autowired
    private AmadeusService amadeusService;

    @GetMapping("/airports")
    public ResponseEntity<?> getAirports(@RequestParam String query) {
        try {
            List<AirportDTO> airports = amadeusService.searchAirports(query);
            return ResponseEntity.ok(airports);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while fetching data: " + e.getMessage());
        }
    }

    @GetMapping("/flights")
    public ResponseEntity<?> getFlights(
            @RequestParam String departureAirport,
            @RequestParam String arrivalAirport,
            @RequestParam String departureDate,
            @RequestParam(required = false) String returnDate,
            @RequestParam Integer adults,
            @RequestParam String currency,
            @RequestParam Boolean nonStopOnly ) {

        if (returnDate == null) {
            try {
                List<FlightDetailsDTO> flights = amadeusService.searchOneWayFlights(
                        departureAirport,
                        arrivalAirport,
                        departureDate,
                        adults,
                        currency,
                        nonStopOnly
                );
                return ResponseEntity.ok(flights);
            } catch (Exception e ) {
                return ResponseEntity.status(500).body("Internal server error: Failed to fetch flights " + e.getMessage());
            }
        }
        try {
            List<FlightDetailsDTO> flights = amadeusService.searchTwoWayFlights(
                    departureAirport,
                    arrivalAirport,
                    departureDate,
                    returnDate,
                    adults,
                    currency,
                    nonStopOnly
            );
            return ResponseEntity.ok(flights);
        } catch (Exception e ) {
            return ResponseEntity.status(500).body("Internal server error: Failed to fetch flights " + e.getMessage());
        }

    }

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().build();
    }
}
