package co.istad.inspectra.features.issue;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.issue.dto.IssuesResponse;
import co.istad.inspectra.features.issue.dto.IssuesWrapperResponse;
import co.istad.inspectra.features.issue.dto.ListIssueResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;



    @Operation(
            summary = "Search for all issues in project",
            description = "This is used for get all issues in a project after scanning."
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<Object> getAllIssuesOnProject(@RequestParam String projectName) throws Exception {

        return BaseRestResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(issueService.getIssueByProjectName(projectName))
                .message("Get issue by project name.")
                .build();

    }



    @Operation(
            summary = "Search specific issue by issueKey",
            description = "This is used for get specific issue detail"
    )
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)

    public BaseRestResponse<Object> getIssueDetail(@RequestParam String issueKey, @RequestParam String ruleKey) throws Exception {

        return BaseRestResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(issueService.getIssueByIssueKey(issueKey, ruleKey))
                .message("Get issue by project name.")
                .build();

    }

    @Operation(
            summary = "Search for all issues in project",
            description = "This is used for get all issues in a project after scanning."
    )
    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<Object> getIssueByProjectFilter(@RequestParam String projectName) throws Exception {

        return BaseRestResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(issueService.getIssueByProjectFilter(projectName))
                .message("Get issue by project name.")
                .build();

    }

    @Operation(
            summary = "Search for all issues in project",
            description = "This is used for get all issues in a project after scanning."
    )
    @GetMapping("/details")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<Object> getIssueDetails() throws Exception {

        return BaseRestResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(issueService.getIssueDetails())
                .message("Get issue by project name.")
                .build();

    }


    @Operation(
            summary = "Search for all issues in project",
            description = "This is used for get all issues in a project after scanning."
    )
    @GetMapping("/flux")
    @ResponseStatus(HttpStatus.OK)
    public Flux<IssuesResponse> getIssueByProjectNameFlux(@RequestParam String projectName) throws Exception {

        return issueService.getIssueByProjectNameFlux(projectName);

    }

    @Operation(
            summary = "Search for all issues in project",
            description = "This is used for get all issues in a project after scanning."
    )
    @GetMapping("/quality")
    @ResponseStatus(HttpStatus.OK)
    public Object getCodeQualityIssues(@RequestParam String projectName) throws Exception {

        return issueService.getCodeQualityIssues(projectName);

    }


    @Operation(summary = "Get all  issues message in component")
    @GetMapping("/message")
    @ResponseStatus(HttpStatus.OK)
    public Flux<ListIssueResponseMessage> getComponentIssuesMessage(@RequestParam String projectName) throws Exception {
        return issueService.getComponentIssuesMessage(projectName);
    }



}
