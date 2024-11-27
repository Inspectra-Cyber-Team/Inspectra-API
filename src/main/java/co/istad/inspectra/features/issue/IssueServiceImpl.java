package co.istad.inspectra.features.issue;

import co.istad.inspectra.features.issue.dto.IssueResponseDto;
import co.istad.inspectra.features.issue.dto.IssuesResponse;
import co.istad.inspectra.features.issue.dto.IssuesWrapperResponse;
import co.istad.inspectra.features.issue.dto.ListIssueResponseMessage;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.features.rule.RuleService;
import co.istad.inspectra.features.source.SourceService;
import co.istad.inspectra.base.SonaResponse;
import co.istad.inspectra.utils.SonarHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService{

    @Value("${sonar.url}")
    private String sonarUrl;
    @Value("${sonar.token}")
    private String sonarUserToken;

    private final ProjectRepository projectRepository;

    private final SonaResponse sonaResponse;

    private final SourceService sourceService;

    private final RuleService ruleService;

    private final WebClient webClient;

    private final SonarHeaders sonarHeaders;

    @Override
    public Object getIssueByProjectName(String projectName) throws Exception {

//        if(projectRepository.findByProjectName(projectName).isPresent()) {
            // Correct URL to SonarQube API endpoint
            String url = sonarUrl + "/api/issues/search?components=" + projectName;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Bearer " + sonarUserToken);

            return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);
//        }

        //return "Project not found";
    }

    @Override
    public Object getIssueByIssueKey(String issueKey, String ruleKey) throws Exception {

        String url = sonarUrl + "/api/issues/search?issues=" + issueKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + sonarUserToken);

        return IssueResponseDto.builder()
                .rule(ruleService.getRuleDetails(ruleKey))
                .snippetError(sourceService.getCodeIssue(issueKey))
                .issue(sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET))
                .build();


    }

    @Override
    public Object getIssueByProjectFilter(String projectName) throws Exception {

        String url = sonarUrl +  "/api/issues/search?issues=" + projectName ;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + sonarUserToken);

        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);

    }

    @Override
    public Object getIssueDetails() throws Exception {

        String url = sonarUrl + "/api/issues/show";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + sonarUserToken);

        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);


    }

    @Override
    public Object getCodeQualityIssues(String projectName) throws Exception {

        String url = sonarUrl + "/api/issues/search?components="+ projectName+
                "&facets=cleanCodeAttributeCategories,impactSoftwareQualities,codeVariants,severities,types,scopes,statuses,createdAt,files,languages,rules,tags,directories,author,assigned_to_me,sonarsourceSecurity&timeZone=Asia/Bangkok";

        HttpHeaders headers = sonarHeaders.getSonarHeader();

        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);

    }

    @Override
    public Flux<IssuesResponse> getIssueByProjectNameFlux(String projectName) throws Exception {

//        if (projectRepository.findByProjectName(projectName).isPresent()) {

            String url = sonarUrl + "/api/issues/search?components=" + projectName;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Bearer " + sonarUserToken);

            return webClient.method(HttpMethod.GET)
                    .uri(url)
                    .headers(httpHeaders -> httpHeaders.addAll(headers))
                    .retrieve()
                    .bodyToMono(IssuesWrapperResponse.class)
                    .flatMapMany(wrapper -> Flux.fromIterable(wrapper.issues())); // Extracts list of issues and flattens
//        } else {
//            return Flux.empty();
//        }
    }

    @Override
    public Flux<ListIssueResponseMessage> getComponentIssuesMessage(String projectName) throws Exception {

        String url = sonarUrl + "/api/issues/search?components=" + projectName;

        return webClient.method(HttpMethod.GET)
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(sonarHeaders.getSonarHeader()))
                .retrieve()
                .bodyToFlux(ListIssueResponseMessage.class)
                .onErrorResume(throwable -> Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Issues not found", throwable)));

    }




}
