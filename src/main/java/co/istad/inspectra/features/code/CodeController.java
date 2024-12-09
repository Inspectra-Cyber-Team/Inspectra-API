package co.istad.inspectra.features.code;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/codes")

public class CodeController {

    private final CodeService codeService;

    @GetMapping("/component-tree/{projectName}")
    @Operation(summary = "Get component tree")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Flux<Object> getComponentTree(@PathVariable String projectName, @RequestParam(required = false,defaultValue = "1") String page,
                                         @RequestParam(required = false,defaultValue = "25") String size,
                                         @RequestParam(required = false,defaultValue = "") String query
    ) {
        return codeService.getComponentTree(projectName, page, size, query);
    }

    @Operation(summary = "Get sub component tree")
    @GetMapping("/sub-component-tree/{projectName}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public Flux<Object> getSubComponentTree(@PathVariable String projectName, @RequestParam(required = false,defaultValue = "1") String page,
                                            @RequestParam(required = false,defaultValue = "100") String size ,
                                            @RequestParam(required = false,defaultValue = "") String query
    ) {
        return codeService.getSubComponentTree(projectName, page, size, query);
    }

}
