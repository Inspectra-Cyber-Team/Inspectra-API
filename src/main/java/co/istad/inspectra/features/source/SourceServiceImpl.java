package co.istad.inspectra.features.source;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class SourceServiceImpl implements SourceService{

    @Value("${sonar.url}")
    private String sonarUrl;

    @Value("${sonar.token}")
    private String sonarToken;

    private final WebClient webClient;


    private final RestTemplate restTemplate;

    @Override
    public HashMap getCodeIssue(String issueKey) throws Exception{


        if (issueKey == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Issue key is required");
        }


        // Correct URL to SonarQube API endpoint
        String url = sonarUrl + "/api/sources/issue_snippets?issueKey=" + issueKey;

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        headers.set("Authorization", "Bearer " + sonarToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Create the body for the request
//        get response from sonar API
        return new ObjectMapper().readValue(restTemplate.exchange(url, HttpMethod.GET,entity,String.class).getBody(), HashMap.class);
    }

    @Override
    public Flux<Object> getSourceCode(String component) throws Exception {

        if (component == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Component is required");
        }

        return webClient.get()
                .uri(sonarUrl + "/api/sources/show?key=" + component)
                .header("Authorization", "Bearer " + sonarToken)
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> Flux.empty());

    }


    @Override
    public Flux<Object> getSourceLinesCode(String componentKey) {

        if (componentKey == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Component key is required");
        }

        return webClient.get()
                .uri(sonarUrl + "/api/sources/lines?key=" + componentKey)
                .header("Authorization", "Bearer " + sonarToken)
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> {
                       throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        });
    }

}
