package co.istad.inspectra.features.qualitygates;

import co.istad.inspectra.base.BaseRestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quality-gates")
@RequiredArgsConstructor
public class QualityGatesController {

    private final QualityGatesService qualityGatesService;

    @GetMapping("{projectName}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<Object> getQualityGatesByProjectName(@PathVariable  String projectName) throws Exception {
        return BaseRestResponse.builder()
                .data(qualityGatesService.getQualityGatesByProjectName(projectName))
                .message("Get quality gates by project name.")
                .build();
    }




}
