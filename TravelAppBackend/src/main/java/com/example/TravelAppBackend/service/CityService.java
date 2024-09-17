package com.example.TravelAppBackend.service;

import com.example.TravelAppBackend.DTO.AmadeusAirportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CityService {

    private final RestTemplate restTemplate;
    private final TokenService tokenService;

    @Autowired
    public CityService(RestTemplate restTemplate, TokenService tokenService) {
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }

    public String getCityNameByCode(String cityCode) {
        String baseUrl = "https://test.api.amadeus.com/v1/reference-data/locations";
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("subType", "CITY")
                .queryParam("sort", "analytics.travelers.score")
                .queryParam("view", "LIGHT")
                .queryParam("keyword", cityCode)
                .toUriString();

        String accessToken;

        try {
            accessToken = tokenService.getAccessToken();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get access token", e.getCause());
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

        return apiResponse.getLocations().get(0).getDetailedName();
    }
}
