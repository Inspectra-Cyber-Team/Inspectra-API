package co.istad.inspectra.features.project;

import co.istad.inspectra.features.project.dto.ProjectRequest;
import co.istad.inspectra.features.project.dto.ProjectResponse;
import co.istad.inspectra.features.project.dto.ProjectUpdateDto;
import co.istad.inspectra.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import reactor.core.publisher.Flux;


import java.util.List;

/**
 * ProjectService is an interface that contains method to create, get all, get by project name and delete project
 * @Author : Lyhou
 * @since  : 1.0 (2024)
 */

public interface ProjectService {

    /**
     * Create a new project
     * @param projectRequest is the request body
     * @return ProjectResponse
     * @Author : Lyhou
     */
   ProjectResponse createProject(@AuthenticationPrincipal CustomUserDetails customUserDetails,ProjectRequest projectRequest) throws Exception;

    /**
     * Get all project
     * @return List<ProjectResponse>
     * @Author  : Lyhou
     */
   List<ProjectResponse> getAllProject(@AuthenticationPrincipal CustomUserDetails customUserDetails);


   /**
    * Get project by project name
    * @return ProjectResponse
    * @Author : Lyhou
    */
   ProjectResponse getProjectByProjectName(String projectName);


   /**
    * Delete project by project name
    * @param projectName is parameter used to delete project
    * @return ProjectResponse
    * @Author : Lyhou
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


    Object getAllProject1() throws Exception;


    Object getProjectFavorite() throws Exception;


    String  removeFavorite(String projectKey) throws Exception;

    Flux<Object> getSecurityHotspot(String projectName) throws Exception;
    Flux<Object> getProjectBranch(String projectName) throws Exception;
    Flux<Object> getProjectWarning(String projectName) throws Exception;
    Flux<Object> getProjectOverview(String projectName) throws Exception;

    Flux<Object> getFacets() throws Exception;

    List<ProjectResponse> getProjectByUserUid(String userUid) throws Exception;





}
