package co.istad.inspectra.features.projectanaly;

import co.istad.inspectra.base.SonaResponse;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.features.projectanaly.dto.LineResponseWrapper;
import co.istad.inspectra.features.projectanaly.dto.LinesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.net.ConnectException;

@Service
@RequiredArgsConstructor
public class ProjectAnalyzeServiceImpl implements ProjectAnalyzeService {

    @Value("${sonar.url}")
    private String sonarUrl;
    @Value("${sonar.token}")
    private String sonarUserToken;

    private final ProjectRepository projectRepository;

    private final SonaResponse sonaResponse;

    private final WebClient webClient;
    @Override
    public Object analyzeProject(String projectName) throws Exception {

        String url = sonarUrl + "/api/project_analyses/search?project=" + projectName;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + sonarUserToken);


        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);
    }

    @Override
    public Object duplicateProject(String projectKey) throws Exception {

        String url = sonarUrl + "/api/duplications/show?key=" + projectKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + sonarUserToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);
    }

    @Override
    public Object getMeasures(String projectKey) throws Exception {

        String url = sonarUrl + "/api/measures/component?component=" + projectKey +
                "&metricKeys=security_rating,reliability_rating,sqale_rating,security_hotspots_reviewed,coverage,duplicated_lines_density";

        HttpHeaders headers = getHeaders();

        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);
    }

    @Override
    public Object getNumberLines(String projectKey) throws Exception {

        String url = sonarUrl + "/api/measures/component?component=" + projectKey +
                "&metricKeys=ncloc,lines,ncloc_language_distribution";

        HttpHeaders headers = getHeaders();

        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);


    }

    @Override
    public Object getProjectOverview(String projectKey) throws Exception {

        String url = sonarUrl + "/api/measures/component?component="+projectKey+"&metricKeys=security_rating,reliability_rating,sqale_rating,coverage,duplicated_lines_density,code_smells,security_hotspots";

        HttpHeaders headers = getHeaders();

        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);


    }

    @Override
    public Flux<LinesResponse> getLines(String projectKey) {

//        projectRepository.findByProjectName(projectKey).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found")
//        );

        String url = sonarUrl + "/api/measures/component?component=" + projectKey +
                "&metricKeys=ncloc,lines,ncloc_language_distribution";

        HttpHeaders headers = getHeaders();

        return webClient.method(HttpMethod.GET)
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(LineResponseWrapper.class)
                .flatMapMany(wrapper -> {

                    String ncloc = wrapper.component().measures().stream()
                            .filter(measure -> "ncloc".equals(measure.metric()))
                            .map(LineResponseWrapper.Measure::value)
                            .findFirst()
                            .orElse(null);

                    String nclocLanguageDistribution = wrapper.component().measures().stream()
                            .filter(measure -> "ncloc_language_distribution".equals(measure.metric()))
                            .map(LineResponseWrapper.Measure::value)
                            .findFirst()
                            .orElse(null);

                    // Return both metrics as a LinesResponse record
                    return Flux.just(new LinesResponse(ncloc, nclocLanguageDistribution));
                })
                .onErrorResume(ConnectException.class, ex ->
                        Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Connection refused: " + ex.getMessage())))

                .onErrorResume(WebClientRequestException.class, ex ->
                        Flux.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Request error: " + ex.getMessage()))
                );
    }



    public HttpHeaders getHeaders() {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        headers.set("Authorization", "Bearer " + sonarUserToken);

        return headers;
    }


}
