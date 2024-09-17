package com.example.TravelAppBackend.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class TokenService {

    @Value("${API_KEY}")
    private String clientId;

    @Value("${API_SECRET}")
    private String clientSecret;

    private String accessToken;
    private Instant tokenExpiry;
    private final Lock lock = new ReentrantLock();

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken() throws Exception {


        if (accessToken == null || Instant.now().isAfter(tokenExpiry)) {
            lock.lock();
            try {
                if (accessToken == null || Instant.now().isAfter(tokenExpiry)) {
                    fetchNewToken();
                }

            } catch (Exception e) {
                throw new Exception("Could not get the token");

            } finally {
                lock.unlock();
            }
        }
        return accessToken;
    }

    private void fetchNewToken() {
        String url = "https://test.api.amadeus.com/v1/security/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

        String requestBody = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s", clientId, clientSecret);



        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<TokenResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, TokenResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            TokenResponse tokenResponse = response.getBody();
            if (tokenResponse != null) {
                accessToken = tokenResponse.getAccess_token();
                tokenExpiry = Instant.now().plusSeconds(tokenResponse.getExpires_in());
            }
        } else {
            throw new RuntimeException("Failed to fetch token: " + response.getStatusCode());
        }
    }

    @Getter
    @Setter
    private static class TokenResponse {
        private String access_token;
        private int expires_in;
    }
}
