package co.istad.inspectra.features.projectanaly;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.projectanaly.dto.LinesResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/project-analyze")
@RequiredArgsConstructor

public class ProjectAnalyzeController {

    private final ProjectAnalyzeService projectAnalyzeService;

    @Operation(
            summary = "Analyze a project"
    )
    @GetMapping("/{projectName}")
    public BaseRestResponse<Object> analyzeProject(@PathVariable String projectName) throws Exception {
        return BaseRestResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(projectAnalyzeService.analyzeProject(projectName))
                .message("Project has been analyzed successfully.")
                .build();
    }


    @Operation(
            summary = "Duplicate a project"
    )
    @GetMapping("/duplicate/{projectKey}")
    public BaseRestResponse<Object> duplicateProject(@PathVariable String projectKey) throws Exception {
        return BaseRestResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(projectAnalyzeService.duplicateProject(projectKey))
                .message("Project has been duplicated successfully.")
                .build();
    }


    @Operation(
            summary = "Get measures of a project"
    )
    @GetMapping("/measures/{projectKey}")
    public BaseRestResponse<Object> getMeasures(@PathVariable String projectKey) throws Exception {
        return BaseRestResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(projectAnalyzeService.getMeasures(projectKey))
                .message("Measures of the project have been fetched successfully.")
                .build();
    }


    @Operation(
            summary = "Get number of lines of a project"
    )
    @GetMapping("/number-lines/{projectKey}")
    public Object getNumberLines(@PathVariable String projectKey) throws Exception {
        return projectAnalyzeService.getNumberLines(projectKey);
    }


    @Operation(
            summary = "Get lines of a project"
    )
    @GetMapping("/lines/{projectKey}")
    public Flux<LinesResponse> getLines(@PathVariable String projectKey) throws Exception {

        return projectAnalyzeService.getLines(projectKey);

    }

    @Operation(
            summary = "Get overview of a project"
    )
    @GetMapping("/overview/{projectKey}")
    public Object getProjectOverview(@PathVariable String projectKey) throws Exception {
        return projectAnalyzeService.getProjectOverview(projectKey);
    }






}
