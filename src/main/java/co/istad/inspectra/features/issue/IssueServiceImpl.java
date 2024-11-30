package co.istad.inspectra.features.issue;

import co.istad.inspectra.domain.Project;
import co.istad.inspectra.features.issue.dto.IssueResponseDto;
import co.istad.inspectra.features.issue.dto.IssuesResponse;
import co.istad.inspectra.features.issue.dto.IssuesWrapperResponse;
import co.istad.inspectra.features.issue.dto.ListIssueResponseMessage;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.features.rule.RuleService;
import co.istad.inspectra.features.source.SourceService;
import co.istad.inspectra.base.SonaResponse;
import co.istad.inspectra.utils.SonarHeadersUtil;
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

    private final SonarHeadersUtil sonarHeadersUtil;

    @Override
    public Object getIssueByProjectName(String projectName, int page, int size, String cleanCodeAttributeCategories, String impactSoftwareQualities, String impactSeverities, String scopes) throws Exception {

        if (page < 0 || size < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page and size must be greater than 0");
        }

        Project project = projectRepository.findByProjectName(projectName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (project.getIsUsed())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project is already in use");
        }

            String url = sonarUrl + "/api/issues/search?components=" + projectName;

            if (page > 0 && size > 0) {
                url += "&p=" + page + "&ps=" + size;
            }

            if (cleanCodeAttributeCategories != null) {
                url += "&cleanCodeAttributeCategories=" + cleanCodeAttributeCategories;
            }

            if (impactSoftwareQualities != null) {
                url += "&impactSoftwareQualities=" + impactSoftwareQualities;
            }

            if (impactSeverities != null) {
                url += "&impactSeverities=" + impactSeverities;
            }

            if (scopes != null) {
                url += "&scopes=" + scopes;
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Bearer " + sonarUserToken);

            return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);

    }

    @Override
    public Object getIssueByIssueKey(String issueKey, String ruleKey) throws Exception {

        if (issueKey == null || ruleKey == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Issue key and rule key must not be null");
        }

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
    public Object getIssueDetails(String issueKey) throws Exception {

        if (issueKey == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Issue key must not be null");
        }

        String url = sonarUrl + "/api/issues/search?issues=" + issueKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + sonarUserToken);

        return sonaResponse.responseFromSonarAPI(url, null, headers, HttpMethod.GET);


    }

    @Override
    public Object getIssue(String projectName,int page,int size,String cleanCodeAttributeCategories, String impactSoftwareQualities, String impactSeverities,
                           String scopes, String types, String languages, String directories, String rules, String issuesStatuses, String tags, String files,
                           String assigned, String createdInLast) throws Exception {

        if (page < 0 || size < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page and size must be greater than 0");
        }

        String url = sonarUrl + "/api/issues/search?components="+ projectName+
                "&facets=cleanCodeAttributeCategories,impactSoftwareQualities,codeVariants,severities,types,scopes,statuses,createdAt,files,languages,rules,tags,directories,author,assigned_to_me,sonarsourceSecurity&timeZone=Asia/Bangkok";

        if (page > 0 && size > 0) {
            url += "&p=" + page + "&ps=" + size;
        }

        if (cleanCodeAttributeCategories != null) {
            url += "&cleanCodeAttributeCategories=" + cleanCodeAttributeCategories;
        }

        if (impactSoftwareQualities != null) {
            url += "&impactSoftwareQualities=" + impactSoftwareQualities;
        }

        if (impactSeverities != null) {
            url += "&impactSeverities=" + impactSeverities;
        }

        if (scopes != null) {
            url += "&scopes=" + scopes;
        }

        if (types != null) {
            url += "&types=" + types;
        }

        if (languages != null) {
            url += "&languages=" + languages;
        }

        if (directories != null) {
            url += "&directories=" + directories;
        }

        if (rules != null) {
            url += "&rules=" + rules;
        }

        if (issuesStatuses != null) {
            url += "&issuesStatuses=" + issuesStatuses;
        }

        if (tags != null) {
            url += "&tags=" + tags;
        }

        if (files != null) {
            url += "&files=" + files;
        }

        if (assigned != null) {
            url += "&assigned=" + assigned;
        }

        if (createdInLast != null) {
            url += "&createdInLast=" + createdInLast;
        }



        HttpHeaders headers = sonarHeadersUtil.getSonarHeader();

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

    }

    @Override
    public Flux<ListIssueResponseMessage> getComponentIssuesMessage(String projectName) throws Exception {

        String url = sonarUrl + "/api/issues/search?components=" + projectName;

        return webClient.method(HttpMethod.GET)
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(sonarHeadersUtil.getSonarHeader()))
                .retrieve()
                .bodyToFlux(ListIssueResponseMessage.class)
                .onErrorResume(throwable -> Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Issues not found", throwable)));

    }




}
