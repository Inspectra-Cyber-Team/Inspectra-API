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
 * @see ProjectServiceImpl
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


    /**
     * Get all project
     * @return {@link ProjectOverview}
     */
    Object getAllProject1(String projectName) throws Exception;

    /**
     * Get project favorite
     * @return {@link ProjectOverview}
     */

    Flux<Object> getProjectFavorite();

  /**   * Remove favorite project
     * @param projectKey is the request body
     * @return {@link String}
     */
    String  removeFavorite(String projectKey) ;

    /**
     * Get project security hotspot
     * @param projectName is the request body
     * @return {@link ProjectOverview}
     */
    Flux<Object> getSecurityHotspot(String projectName);

    /**
     * Get project branch
     * @param projectName is the request body
     * @return {@link ProjectOverview}
     */
    Flux<Object> getProjectBranch(String projectName) ;

    /**
     * Get project warning
     * @param projectName is the request body
     * @return {@link ProjectOverview}
     */
    Flux<Object> getProjectWarning(String projectName) ;

    /**
     * Get project overview
     * @param projectName is the request body
     * @return {@link ProjectOverview}
     */
    Mono<ProjectOverview> getProjectOverview(String projectName);

    /**
     * Get project by project name
     * @return {@link ProjectResponse}
     */
    Flux<Object> getProjectDetails(String projectName);

    /**
     * Get project by project name
     * @return {@link ProjectResponse}
     */
    Flux<Object> getFacets() throws Exception;

    /**
     * Get project by user uid
     * @param userUid is the request body
     * @param page is the request body
     * @param size is the request body
     * @return {@link ProjectOverview}
     */
    Flux<ProjectOverview> getProjectByUserUid(String userUid, int page, int size);


    /**
     * cout all project
     * @return int
     */
    int countAllProject();



}
