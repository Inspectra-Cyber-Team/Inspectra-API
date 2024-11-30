package co.istad.inspectra.features.project;

import co.istad.inspectra.features.project.dto.ProjectOverview;
import co.istad.inspectra.features.project.dto.ProjectRequest;
import co.istad.inspectra.features.project.dto.ProjectResponse;
import co.istad.inspectra.features.project.dto.ProjectUpdateDto;
import co.istad.inspectra.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;

/**
 * ProjectService is an interface that contains method to create, get all, get by project name and delete project
 * @author : Lyhou
 * @since  : 1.0 (2024)
 */

public interface ProjectService {

    /**
     * Create a new project
     * @param projectRequest is the request body
     * @return ProjectResponse
     * @author : Lyhou
     */
   ProjectResponse createProject(@AuthenticationPrincipal CustomUserDetails customUserDetails,ProjectRequest projectRequest) throws Exception;

    /**
     * Get all project
     * @return List<ProjectResponse>
     *
     */
   List<ProjectResponse> getAllProject(@AuthenticationPrincipal CustomUserDetails customUserDetails);


   /**
    * Get project by project name
    * @return ProjectResponse
    *
    */
   ProjectResponse getProjectByProjectName(String projectName);


    List<ProjectResponse> createProjectForNonUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception;


   /**
    * Delete project by project name
    * @param projectName is parameter used to delete project
    * @return ProjectResponse
    *
    */

   String deleteProjectByProjectName(String projectName);


    /**
     * Update project name
     * @param projectUpdateDto is the request body
     * @return {@link ProjectResponse}
     */

   ProjectResponse updateProjectName(ProjectUpdateDto projectUpdateDto);


    /**
     *
     * Favorite project
     * @param projectName is the request body
     */

    String favoriteProject(String projectName);


    Object getAllProject1(String projectName) throws Exception;


    Flux<Object> getProjectFavorite();


    String  removeFavorite(String projectKey) ;

    Flux<Object> getSecurityHotspot(String projectName);
    Flux<Object> getProjectBranch(String projectName) ;
    Flux<Object> getProjectWarning(String projectName) ;
    Mono<ProjectOverview> getProjectOverview(String projectName);
    Flux<Object> getProjectDetails(String projectName);

    Flux<Object> getFacets() throws Exception;

    Flux<ProjectOverview> getProjectByUserUid(String userUid);





}
