package com.example.TravelAppBackend.service;

import com.example.TravelAppBackend.ApiFlightResponse.ApiResponse;
import com.example.TravelAppBackend.DTO.*;
import com.example.TravelAppBackend.utils.FlightDetailsMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AmadeusService {

    @Autowired
    private TokenService tokenService;
    private final RestTemplate restTemplate;
    private final FlightDetailsMapper flightDetailsMapper;
    private final CityService cityService;

    @Autowired
    public AmadeusService(RestTemplate restTemplate, FlightDetailsMapper flightDetailsMapper, CityService cityService) {
        this.restTemplate = restTemplate;
        this.flightDetailsMapper = flightDetailsMapper;
        this.cityService = cityService;
    }

    public List<FlightDetailsDTO> searchOneWayFlights(
            String departureAirport,
            String arrivalAirport,
            String departureDate,
            Integer numberOfPassangers,
            String currency,
            Boolean nonStop
            ) {

        String baseUrl = "https://test.api.amadeus.com/v2/shopping/flight-offers";
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("originLocationCode", departureAirport)
                .queryParam("destinationLocationCode", arrivalAirport)
                .queryParam("departureDate", departureDate)
                .queryParam("adults", numberOfPassangers)
                .queryParam("currencyCode", currency)
                .queryParam("nonStop", nonStop)
                .queryParam("max", 50)
                .toUriString();
        String accessToken;

        try {
            accessToken = tokenService.getAccessToken();
        } catch (Exception e) {
            throw new RuntimeException("Failed getting the token", e);
        }

        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("Empty or null access token");

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.valueOf("application/vnd.amadeus+json"));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.GET, entity, String.class);

        String jsonResponse = response.getBody();

        ApiResponse flightResponse;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            flightResponse = objectMapper.readValue(jsonResponse, ApiResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while processing Json", e);
        }



        assert flightResponse != null;


        if (flightResponse.getMeta().getCount() > 0) {
            return flightDetailsMapper.mapToFlightDetailsDTOList(flightResponse);

        }else {
            return new ArrayList<FlightDetailsDTO>();
        }

    }

    public List<FlightDetailsDTO> searchTwoWayFlights(
            String departureAirport,
            String arrivalAirport,
            String departureDate,
            String returnDate,
            Integer numberOfPassangers,
            String currency,
            Boolean nonStop) {

        String baseUrl = "https://test.api.amadeus.com/v2/shopping/flight-offers";
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("originLocationCode", departureAirport)
                .queryParam("destinationLocationCode", arrivalAirport)
                .queryParam("departureDate", departureDate)
                .queryParam("returnDate", returnDate)
                .queryParam("adults", numberOfPassangers)
                .queryParam("currencyCode", currency)
                .queryParam("nonStop", nonStop)
                .queryParam("max", 25)
                .toUriString();
        String accessToken;

        try {
            accessToken = tokenService.getAccessToken();
        } catch (Exception e) {
            throw new RuntimeException("Failed getting the token", e);
        }

        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("Empty or null access token");

        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ApiResponse> response = restTemplate.exchange(url,HttpMethod.GET, entity, ApiResponse.class );

        ApiResponse apiResponse = response.getBody();

        assert apiResponse != null;

        if (apiResponse.getMeta().getCount() > 0) {
            return flightDetailsMapper.mapToFlightDetailsDTOList(apiResponse);

        }else {
            return new ArrayList<FlightDetailsDTO>();
        }



    }


    public List<AirportDTO> searchAirports(String query) throws RuntimeException {

        String baseUrl = "https://test.api.amadeus.com/v1/reference-data/locations";
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("subType", "AIRPORT")
                .queryParam("sort", "analytics.travelers.score")
                .queryParam("view", "LIGHT")
                .queryParam("keyword", query)
                .toUriString();

        String accessToken;

        try {
            accessToken = tokenService.getAccessToken();

        } catch (Exception e) {
            throw new RuntimeException("Failed to get access token" , e.getCause());
        }

        if (accessToken == null || accessToken.isEmpty()) {
            throw new RuntimeException("Access Token is null or empty");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.add("Content-Type", "application/vnd.amadeus+json");


        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<AmadeusAirportResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, AmadeusAirportResponse.class);

        AmadeusAirportResponse apiResponse = response.getBody();
        assert apiResponse != null;


        return apiResponse.getLocations().stream()
                .map(airport -> new AirportDTO(
                        airport.getAddress().getCityName() + ", " + airport.getAddress().getCountryName(),
                        airport.getName(),
                        airport.getIataCode()
                ))
                .collect(Collectors.toList());
    }
}
