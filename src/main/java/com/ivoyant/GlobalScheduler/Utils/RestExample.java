package com.ivoyant.GlobalScheduler.Utils;

import com.ivoyant.GlobalScheduler.Model.ExceptionMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestExample {

    public ResponseEntity<String> restCall(String apiUrl, HttpMethod httpMethod) {
        ResponseEntity<String> responseEntity = null;
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Create the request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Add any other required headers

            // Create the request entity with headers
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            // Send the HTTP GET request
            responseEntity = restTemplate.exchange(apiUrl, httpMethod, requestEntity, String.class);

            // Get the response body
        } catch (RuntimeException e) {
            int responseCode = ExceptionMapper.valueOf(e.getClass().getSimpleName()).getStatusCode();
            responseEntity = ResponseEntity.status(responseCode).body(e.getMessage());
        }
        // Process the response data
        return responseEntity;
    }
}