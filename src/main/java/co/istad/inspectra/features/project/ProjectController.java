package co.istad.inspectra.features.project;


import co.istad.inspectra.features.project.dto.ProjectRequest;
import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.project.dto.ProjectResponse;
import co.istad.inspectra.features.project.dto.ProjectUpdateDto;
import co.istad.inspectra.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor

public class ProjectController {

    private final ProjectService projectService;

    @Operation(
            summary = "Create a project"
    )
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
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
    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseRestResponse<Object> deleteProjectByName(@Valid @RequestParam String projectName) {

        return BaseRestResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NO_CONTENT.value())
                .data(projectService.deleteProjectByProjectName(projectName))
                .message("Project " + projectName + " has been deleted successfully.")
                .build();
    }


    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
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
    @GetMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<List<ProjectResponse>> findProjectByUserUuid(@Valid @RequestParam String userUuid)  {
        return BaseRestResponse
                .<List<ProjectResponse>>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(projectService.getProjectByUserUid(userUuid))
                .message("Project has been retrieved successfully.")
                .build();
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
    public Flux<Object> getProjectFavorite() throws Exception {

        return projectService.getProjectFavorite();

    }

    @Operation(
            summary = "Get project favorit",
            description = "This endpoint is used for getting project favorit"
    )
    @PostMapping("/remove/favorite")
    @ResponseStatus(HttpStatus.OK)
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
    @GetMapping("/security/hotspot")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> getSecurityHotspot(@Valid @RequestParam String projectName){
        return projectService.getSecurityHotspot(projectName);
    }


    @Operation(summary = "Get project branch", description = "This endpoint is used for getting project branch")
    @GetMapping("/branch")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> getProjectBranch(@Valid @RequestParam String projectName){
        return projectService.getProjectBranch(projectName);
    }


    @Operation(summary = "Get project warning", description = "This endpoint is used for getting project warning")
    @GetMapping("/warning")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> getProjectWarning(@Valid @RequestParam String projectName) {
        return projectService.getProjectWarning(projectName);
    }

    @Operation(summary = "Get project overview", description = "This endpoint is used for getting project overview")
    @GetMapping("/overview")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> getProjectOverview(@Valid @RequestParam String projectName)  {
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







}
