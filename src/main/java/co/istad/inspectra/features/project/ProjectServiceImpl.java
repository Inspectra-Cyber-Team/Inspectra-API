package co.istad.inspectra.features.project;

import co.istad.inspectra.domain.Project;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.project.dto.*;
import co.istad.inspectra.base.SonaResponse;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.mapper.ProjectMapper;
import co.istad.inspectra.security.CustomUserDetails;
import co.istad.inspectra.utils.SonarHeadersUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.*;

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

    private final SonarHeadersUtil sonarHeadersUtil;

    private final WebClient webClient;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ProjectResponse createProject(@AuthenticationPrincipal CustomUserDetails customUserDetails, ProjectRequest projectRequest) throws Exception {

         if(customUserDetails == null){

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");

         }

         String email = customUserDetails.getUsername();

         User user = userRepository.findUsersByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (projectRepository.existsByProjectName(projectRequest.projectName())) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ProjectName already existed");

        }


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
    public List<ProjectResponse> createProjectForNonUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception {

        User user;

        // Check if the user is logged in
        if (customUserDetails == null) {
            // Handle non-logged-in user (auto-create guest user with new UUID)
            user = userRepository.save(User.builder()
                    .uuid(UUID.randomUUID().toString()) // Generate new UUID for each request
                    .firstName("Guest+")
                    .lastName("User")
                    .email("guest+"+UUID.randomUUID().toString()+"@default.com")
                    .password(passwordEncoder.encode("default-password")) // Ensure encryption here
                    .isVerified(true)
                    .isDeleted(false)
                    .isActive(true)
                    .registeredDate(LocalDateTime.now())
                    .isAccountNonExpired(true)
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(true)
                    .isEnabled(true)
                    .build());

            // Dynamically create unique project names for the guest user
            List<String> defaultProjectNames = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                String projectName = "Guest_Project_" + user.getUuid() + "_" + i;
                defaultProjectNames.add(projectName);
            }

            List<ProjectResponse> projectResponses = new ArrayList<>();

            // Loop through and create projects
            for (String projectName : defaultProjectNames) {
                if (projectRepository.existsByProjectName(projectName)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ProjectName already exists: " + projectName);
                }

                // Call SonarQube API
                String url = sonarUrl + "/api/projects/create";
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                httpHeaders.set("Authorization", "Bearer " + sonarToken);

                String sanitizedProjectName = projectName.replace(" ", "-");
                String body = "project=" + sanitizedProjectName +
                        "&name=" + sanitizedProjectName +
                        "&visibility=" + "public";

                // Save the project in the database
                Project project = projectRepository.save(
                        Project.builder()
                                .projectName(sanitizedProjectName)
                                .uuid(UUID.randomUUID().toString())
                                .user(user)
                                .isDeleted(false)
                                .isUsed(false)
                                .build()
                );

                // Call external API
                sonaResponse.responseFromSonarAPI(url, body, httpHeaders, HttpMethod.POST);

                // Add the project response to the list
                projectResponses.add(projectMapper.mapToProjectResponse(project));
            }

            return projectResponses;
        } else {
            // If logged-in user, let them create a single project (or handle differently if needed)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This endpoint is for guest users only.");
        }
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

        HttpHeaders httpHeaders = sonarHeadersUtil.getSonarHeader();

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
    public Object getAllProject1(String projectName) throws Exception {

        Project project = projectRepository.findByProjectName(projectName)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectName not found"));

        String url = sonarUrl + "/api/projects/search?projects="+projectName;

        HttpHeaders httpHeaders = sonarHeadersUtil.getSonarHeader();

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

        HttpHeaders httpHeaders = sonarHeadersUtil.getSonarHeader();

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

        return getObjectFlux(projectName, url);


    }

    @NotNull
    private Flux<Object> getObjectFlux(String projectName, String url) {
        return Mono.fromCallable(() -> projectRepository.findByProjectName(projectName)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ProjectName not found")))
                .subscribeOn(Schedulers.boundedElastic())

                // Moves the blocking call to a separate thread pool
                .flatMapMany(project -> webClient.get()
                        .uri(url)
                        .headers(headers -> headers.setBearerAuth(sonarToken))
                        .retrieve()
                        .bodyToFlux(Object.class) // Returns Flux<Object>
                        .onErrorResume(e -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage()))));
    }

    @Override
    public Flux<Object> getProjectBranch(String projectName)  {

        String url = sonarUrl + "/api/project_branches/list?project=" + projectName;

        return getObjectFlux(projectName, url);


    }

    @Override
    public Flux<Object> getProjectWarning(String projectName)  {

        String url = sonarUrl + "/api/ce/analysis_status?component=" + projectName;

        return getObjectFlux(projectName, url);


    }

//    @Override
//    public Flux<Object> getProjectOverview(String projectName) {
//        // Use Mono.defer() to handle the blocking operation
//
//        String url = sonarUrl + "/api/measures/component?component=" + projectName + "&metricKeys=ncloc,security_issues,reliability_issues,maintainability_issues,vulnerabilities,bugs,code_smells,security_hotspots,coverage,duplicated_lines_density,accepted_issues";
//
//        return getObjectFlux(projectName, url);
//
//    }

    @Override
    public Mono<ProjectOverview> getProjectOverview(String projectName) {
        // First API endpoint
        String measuresUrl = sonarUrl + "/api/measures/component?component=" + projectName +
                "&metricKeys=ncloc,security_issues,reliability_issues,maintainability_issues,vulnerabilities,bugs,code_smells,security_hotspots,coverage,duplicated_lines_density,accepted_issues";

        // Second API endpoint
        String branchUrl = sonarUrl + "/api/project_branches/list?project=" + projectName;

        // Call both APIs using `getObjectFlux` method
        Mono<Object> measuresMono = getObjectFlux(projectName, measuresUrl).next(); // Get the first result

        Mono<List<Object>> branchesMono = getObjectFlux(projectName, branchUrl).collectList(); // Collect as List

        // Combine the results into a single Mono with the desired structure
        return Mono.zip(measuresMono,branchesMono,ProjectOverview::new);

    }

    @Override
    public Flux<Object> getProjectDetails(String projectName) {

        String measuresUrl = sonarUrl + "/api/measures/component?component=" + projectName +
                "&metricKeys=ncloc,security_issues,reliability_issues,maintainability_issues,vulnerabilities,bugs,code_smells,security_hotspots,coverage,duplicated_lines_density,accepted_issues";

        return getObjectFlux(projectName, measuresUrl);
    }


    @Override
    public Flux<Object> getFacets()  {


        return webClient.get()
                .uri(sonarUrl + "/api/components/search_projects?ps=50&facets=reliability_rating,security_rating,security_review_rating,coverage,duplicated_lines_density,ncloc,alert_status,languages,tags,qualifier&f=analysisDate,leakPeriodDate&s=creationDate&asc=false")
                .headers(headers -> headers.setBearerAuth(sonarToken))
                .retrieve()
                .bodyToFlux(Object.class)
                .onErrorResume(e -> Flux.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while sending request: " + e.getMessage())));
    }


    @Override
    public Flux<ProjectOverview> getProjectByUserUid(String userUid) {
        // Find user by UUID
        return Mono.fromCallable(() -> userRepository.findUserByUuid(userUid))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")))
                .flatMapMany(user -> {
                    // Find projects by user UUID
                    List<Project> projectList = projectRepository.findByUserUuid(user.getUuid());

                    if (projectList.isEmpty()) {
                        return Flux.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Project list is empty"));
                    }

                    // Process each project reactively
                    return Flux.fromIterable(projectList)
                            .flatMap(project -> getProjectOverview(project.getProjectName()));
                });
    }






}

