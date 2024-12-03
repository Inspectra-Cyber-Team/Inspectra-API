package co.istad.inspectra.features.project;


import co.istad.inspectra.features.project.dto.ProjectOverview;
import co.istad.inspectra.features.project.dto.ProjectRequest;
import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.project.dto.ProjectResponse;
import co.istad.inspectra.features.project.dto.ProjectUpdateDto;
import co.istad.inspectra.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor

public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "Create a project")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<ProjectResponse> createProject(@AuthenticationPrincipal CustomUserDetails customUserDetails,@Valid @RequestBody ProjectRequest projectRequest) throws Exception {

        return BaseRestResponse
                .<ProjectResponse>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED.value())
                .data(projectService.createProject(customUserDetails,projectRequest))
                .message("Project has been created successfully.")
                .build();
    }



    @Operation(
            summary = "Get all projects",
            description = "This endpoint is used for listing all projects"
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<List<ProjectResponse>> getAllProject(@AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
       return BaseRestResponse
               .<List<ProjectResponse>>builder()
               .timestamp(LocalDateTime.now())
               .status(HttpStatus.OK.value())
               .data(projectService.getAllProject(customUserDetails))
               .message("List all projects")
               .build();
    }


    @Operation(
            summary = "Get project by project name",
            description = "This endpoint is used for getting project by project name"
    )
    @GetMapping("/{projectName}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<ProjectResponse> getProjectByName(@Valid  @PathVariable String projectName) {

        return BaseRestResponse
                .<ProjectResponse>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(projectService.getProjectByProjectName(projectName))
                .message("Project has been retrieved successfully.")
                .build();
    }


    @Operation(
            summary = "Delete project by project name",
            description = "This endpoint is used for deleting project by project name"
    )
    @DeleteMapping("{projectName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<Object> deleteProjectByName(@Valid @PathVariable String projectName) {

        return BaseRestResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NO_CONTENT.value())
                .data(projectService.deleteProjectByProjectName(projectName))
                .message("Project " + projectName + " has been deleted successfully.")
                .build();
    }


    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<ProjectResponse> updateProjectName(@Valid @RequestBody ProjectUpdateDto projectUpdateDto) {

        return BaseRestResponse
                .<ProjectResponse>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(projectService.updateProjectName(projectUpdateDto))
                .message("Project has been updated successfully.")
                .build();
    }

    @Operation(summary = "find project by user uuid", description = "This endpoint is used for finding project by user uuid")
    @GetMapping("/user/{uuid}")
    @ResponseStatus(HttpStatus.OK)
//    @PreAuthorize("hasAnyRole('ADMIN,USER')")
    public Flux<ProjectOverview> findProjectByUserUuid(@Valid @PathVariable String uuid)  {

        return projectService.getProjectByUserUid(uuid);

    }




    @Operation(
            summary = "Favorite project",
            description = "This endpoint is used for favouring project"
    )
    @PostMapping("/favorite")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<Object> favoriteProject(@Valid @RequestParam String projectName) {

        return BaseRestResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(projectService.favoriteProject(projectName))
                .message("Project " + projectName + " has been favorite successfully.")
                .build();
    }


    @Operation(
            summary = "Get all projects",
            description = "This endpoint is used for listing all projects"
    )
    @GetMapping("{projectName}/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<Object> getAllProject1(@PathVariable String projectName) throws Exception {
       return BaseRestResponse
               .builder()
               .timestamp(LocalDateTime.now())
               .status(HttpStatus.OK.value())
               .data(projectService.getAllProject1(projectName))
               .message("List all projects")
               .build();
    }


    @Operation(
            summary = "Get project favorit",
            description = "This endpoint is used for getting project favorit"
    )
    @GetMapping("/favorite")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Flux<Object> getProjectFavorite() throws Exception {

        return projectService.getProjectFavorite();

    }

    @Operation(
            summary = "Get project favorit",
            description = "This endpoint is used for getting project favorit"
    )
    @PostMapping("/remove/favorite")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<Object> removeFavorite(@Valid @RequestParam String projectKey)  {

       return BaseRestResponse
               .builder()
               .timestamp(LocalDateTime.now())
               .status(HttpStatus.OK.value())
                .data(projectService.removeFavorite(projectKey))
               .message("Success fully remove project from favorite project")
               .build();
    }


    @Operation(summary = "Get security hotspot", description = "This endpoint is used for getting security hotspot")
    @GetMapping("/security-hotspot/{projectName}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> getSecurityHotspot(@Valid @PathVariable String projectName){
        return projectService.getSecurityHotspot(projectName);
    }


    @Operation(summary = "Get project branch", description = "This endpoint is used for getting project branch")
    @GetMapping("/branch/{projectName}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> getProjectBranch(@Valid @PathVariable String projectName){
        return projectService.getProjectBranch(projectName);
    }


    @Operation(summary = "Get project warning", description = "This endpoint is used for getting project warning")
    @GetMapping("/warning/{projectName}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> getProjectWarning(@Valid @PathVariable String projectName) {
        return projectService.getProjectWarning(projectName);
    }

    @Operation(summary = "Get project overview", description = "This endpoint is used for getting project overview")
    @GetMapping("/overview/{projectName}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ProjectOverview> getProjectOverview(@Valid @PathVariable String projectName)  {
        return projectService.getProjectOverview(projectName);
    }

    @Operation(summary = "Get project details", description = "This endpoint is used for getting project details from sonarqube api")
    @GetMapping("/details")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> getProjectDetails(@Valid @RequestParam String projectName) {
        return projectService.getProjectDetails(projectName);
    }

    @Operation(summary = "Get facets", description = "This endpoint is used for getting facets")
    @GetMapping("/facets")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> getFacets() throws Exception {
        return projectService.getFacets();
    }


    @Operation(summary = "Create project for non login user")
    @PostMapping("/create/non_user")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseRestResponse<List<ProjectResponse>> createProjectForNonUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) throws Exception {
        return BaseRestResponse
                .<List<ProjectResponse>>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED.value())
                .data(projectService.createProjectForNonUser(customUserDetails))
                .message("Project has been created successfully.")
                .build();
    }








}
