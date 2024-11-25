package co.istad.inspectra.features.project;

import co.istad.inspectra.domain.Project;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.project.dto.ProjectRequest;
import co.istad.inspectra.features.project.dto.ProjectResponse;
import co.istad.inspectra.base.SonaResponse;
import co.istad.inspectra.features.project.dto.ProjectUpdateDto;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.mapper.ProjectMapper;
import co.istad.inspectra.security.CustomUserDetails;
import co.istad.inspectra.utils.SonarHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    @Value("${sonar.url}")
    private String sonarUrl;

    @Value("${sonar.token}")
    private String sonarToken;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final SonaResponse sonaResponse;
    private final SonarHeaders sonarHeaders;
    private final WebClient webClient;
    private final UserRepository userRepository;

    @Override
    public ProjectResponse createProject(@AuthenticationPrincipal CustomUserDetails customUserDetails, ProjectRequest projectRequest) throws Exception {

         if(customUserDetails == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
            }

         String email = customUserDetails.getUsername();

        User user = userRepository.findUsersByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        String url = sonarUrl + "/api/projects/create";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + sonarToken);

        String projectName = projectRequest.projectName().replace(" ", "-");

        String body = "project=" + projectName +
                "&name=" + projectName +
                "&visibility=" + "public";

        Boolean isProjectAlreadyExisted = projectRepository.existsByProjectName(projectRequest.projectName());

        if(isProjectAlreadyExisted){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ProjectName already existed");
        }


        Project project = projectRepository.save(
                Project.builder()
                        .projectName(projectName)
                        .uuid(UUID.randomUUID().toString())
                        .user(user)
                        .isDeleted(false)
                        .isUsed(false)
                        .build()
        );

        sonaResponse.responseFromSonarAPI(url, body, httpHeaders, HttpMethod.POST);


        return projectMapper.mapToProjectResponse(project);

    }

    @Override
    public List<ProjectResponse> getAllProject(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (customUserDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authorized");
        }

        String email = customUserDetails.getUsername();

        if(projectRepository.findAll().isEmpty()){

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project is empty");

        }

        return projectRepository.findAll().stream()
                .filter(project -> project.getCreatedBy().equals(email))
                .map(projectMapper::mapToProjectResponse)
                .toList();
    }

    @Override
    public ProjectResponse getProjectByProjectName(String projectName) {

        var findProject = projectRepository.findByProjectName(projectName)

                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectName not found"));

        return projectMapper.mapToProjectResponse(findProject);
    }

    @Override
    public String deleteProjectByProjectName(String projectName) {

        String url = sonarUrl + "/api/projects/delete?project=" + projectName;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + sonarToken);

        var entity = new HttpEntity<>(httpHeaders);

      try {

          var response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);


//        check if project name doesn't exist
          if (response.getStatusCode() == HttpStatus.NOT_FOUND) {

              throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectName not found");

          }

          if (response.getStatusCode() == HttpStatus.NO_CONTENT) {

              Project project = projectRepository.findByProjectName(projectName)
                      .orElseThrow(() ->
                              new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectName not found"));

              project.setIsDeleted(true);

              projectRepository.save(project);


              return response.getBody();

          } else {

              throw new RuntimeException("Failed to delete project: " + response.getStatusCode());

          }
      }catch (Exception e) {

          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage());

      }


    }

    @Override
    public ProjectResponse updateProjectName(ProjectUpdateDto projectUpdateDto) {

        Project project = projectRepository.findByProjectName(projectUpdateDto.projectName())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectName not found"));


        String url = sonarUrl + "/api/projects/update_key?from=" + projectUpdateDto.projectName() + "&to=" + projectUpdateDto.newProjectName();

        HttpHeaders httpHeaders = sonarHeaders.getSonarHeader();

        var entity = new HttpEntity<>(httpHeaders);

    try {

        var response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectName not found");

        }

        if (response.getStatusCode() == HttpStatus.OK) {

            project.setProjectName(projectUpdateDto.newProjectName());

            projectRepository.save(project);

            return projectMapper.mapToProjectResponse(project);

        } else {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update project: " + response.getStatusCode());

        }

    }catch (Exception e) {

        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage());

    }


    }

    @Override
    public String favoriteProject(String projectName) {

        Project project = projectRepository.findByProjectName(projectName)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectName not found"));

        project.setIsUsed(true);

        projectRepository.save(project);

        String url = sonarUrl + "/api/favorites/add?component=" + projectName;

//        HttpHeaders httpHeaders = sonarHeaders.getSonarHeader();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + sonarToken);

        var entity = new HttpEntity<>(httpHeaders);

        try {
            // Send the request
            var response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // Check response status
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to favorite project: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage());
        }


    }

    @Override
    public Object getAllProject1() throws Exception {

        String url = sonarUrl + "/api/projects?s=creationDate";
        HttpHeaders httpHeaders = sonarHeaders.getSonarHeader();

        return sonaResponse.responseFromSonarAPI(url, null, httpHeaders, HttpMethod.GET);

    }

    @Override
    public Flux<Object> getProjectFavorite(){

//        String uuid = customUserDetails.getUserUuid();
//
//        if(projectRepository.findAll().isEmpty()){
//
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project is empty");
//
//        }
//
//        List<Project> project = projectRepository.findByUserUuid(uuid);
//
//        if(project.isEmpty()){
//
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project is empty");
//
//        }


        String url = sonarUrl + "/api/favorites/search";

        return webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToFlux(Object.class);




    }

    @Override
    public String removeFavorite(String projectKey)  {

        String url = sonarUrl + "/api/favorites/remove?component=" + projectKey;

        HttpHeaders httpHeaders = sonarHeaders.getSonarHeader();

        var entity = new HttpEntity<>(httpHeaders);


        try {

        var response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if(response.getStatusCode().is2xxSuccessful()){

            return response.getBody();

        } else {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to remove favorite project: " + response.getStatusCode());
        }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage());
        }

    }

    @Override
    public Flux<Object> getSecurityHotspot(String projectName)  {


        String url = sonarUrl + "/api/hotspots/search?project="+projectName;

        return webClient.get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage())));


    }

    @Override
    public Flux<Object> getProjectBranch(String projectName)  {


        return webClient.get()
                .uri(sonarUrl + "/api/project_branches/list?project=" + projectName)
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage())));
    }

    @Override
    public Flux<Object> getProjectWarning(String projectName)  {

        return webClient.get()
                .uri(sonarUrl + "/api/ce/analysis_status?component=" + projectName)
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage())));

    }

    @Override
    public Flux<Object> getProjectOverview(String projectName) {

        return webClient.get()
                .uri(sonarUrl + "/api/measures/component?component="+projectName+"&metricKeys=ncloc,security_issues,reliability_issues,maintainability_issues,vulnerabilities,bugs,code_smells,security_hotspots,coverage,duplicated_lines_density")
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage())));
    }

    @Override
    public Flux<Object> getFacets()  {


        return webClient.get()
                .uri(sonarUrl + "/api/components/search_projects?ps=50&facets=reliability_rating,security_rating,security_review_rating,coverage,duplicated_lines_density,ncloc,alert_status,languages,tags,qualifier&f=analysisDate,leakPeriodDate&filter=query=la&s=creationDate&asc=false")
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage())));
    }

    @Override
    public List<ProjectResponse> getProjectByUserUid(String userUid) {

        User user = userRepository.findUserByUuid(userUid);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        List<Project> project = projectRepository.findByUserUuid(user.getUuid());

        if(project.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project is empty");
        }

        return project.stream()
                .map(projectMapper::mapToProjectResponse)
                .toList();



    }





}

