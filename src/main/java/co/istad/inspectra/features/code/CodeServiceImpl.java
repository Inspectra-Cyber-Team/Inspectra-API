package co.istad.inspectra.features.code;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor

public class CodeServiceImpl implements CodeService {

    @Value("${sonar.url}")
    private String sonarUrl;
    @Value("${sonar.token}")
    private String sonarUserToken;

    private final WebClient webClient;
    @Override
    public Flux<Object> getComponentTree(String projectName,String page,String pageSize,String query) {

        return fetchComponentTree(projectName, page, pageSize, query);

    }

    @Override
    public Flux<Object> getSubComponentTree(String projectName, String page, String pageSize, String query) {

        return fetchComponentTree(projectName, page, pageSize, query);

    }

    private Flux<Object> fetchComponentTree(String projectName, String page, String pageSize, String query) {
        // Construct the URL using string concatenation
        String url = sonarUrl + "/api/measures/component_tree?ps=" + pageSize
                + "&s=qualifier,name&p=" + page
                + "&component=" + projectName
                + "&metricKeys=ncloc,security_issues,reliability_issues,maintainability_issues,vulnerabilities,bugs,code_smells,security_hotspots,coverage,duplicated_lines_density&strategy=children";

        // Add query parameter if present
        if (query != null && !query.isEmpty()) {
            url += "&q=" + query;
        }

        // Perform the WebClient request
        return webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + sonarUserToken)
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
                });
    }



}
