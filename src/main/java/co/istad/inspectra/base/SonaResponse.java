package co.istad.inspectra.base;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;

@Configuration
@RequiredArgsConstructor
public class SonaResponse {

    private final RestTemplate restTemplate = new RestTemplate();
    public HashMap<String, Object> responseFromSonarAPI(String url, String body, HttpHeaders headers, HttpMethod httpMethod) throws Exception {


        var entity = new HttpEntity<>(body, headers);
        // Correct URL variable usage
        var response = restTemplate.exchange(url, httpMethod, entity, String.class);

        if(response.getStatusCode().isError()){
            // Handle HTTP error by throwing ResponseStatusException
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Error in response from SonarQube API: " + response.getStatusCode());
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Parse the JSON response into a HashMap
            return objectMapper.readValue(response.getBody(), HashMap.class);

        } catch (JsonProcessingException e) {
            // Handle JSON parsing error by throwing ResponseStatusException
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error parsing JSON response from SonarQube API: " + e.getMessage(), e);
        } catch (Exception e) {
            // Catch any other exceptions and throw ResponseStatusException
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error occurred: " + e.getMessage(), e);
        }
    }

}

