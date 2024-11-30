package co.istad.inspectra.features.source;


import co.istad.inspectra.base.BaseRestResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/sources")
@RequiredArgsConstructor

public class SourceController {

    private final SourceService sourceService;


    @Operation(
            summary = "Get code snippets issues",
            description = "This endpoint used issueKey to get issues about codes, the issueKey get from searching all issues in a project"
    )
    @GetMapping("{issueKey}")
    @ResponseStatus(HttpStatus.OK)

    public BaseRestResponse<Object> getCodeSnippetIssue(@PathVariable String issueKey) throws Exception{

        return BaseRestResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(sourceService.getCodeIssue(issueKey))
                .message("Get code issues in file.")
                .build();
    }


    @Operation(
            summary = "Get source code",
            description = "This endpoint used component to get source code of a project"
    )
    @GetMapping("{componentKey}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> getSourceCode(@PathVariable String componentKey) throws Exception {
        return sourceService.getSourceCode(componentKey);
    }


    @Operation(
            summary = "Get source code lines",
            description = "This endpoint used componentKey to get source code lines of a project"
    )
    @GetMapping("/lines/{componentKey}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Object> getSourceLinesCode(@PathVariable String componentKey) {
        return sourceService.getSourceLinesCode(componentKey);
    }



}
