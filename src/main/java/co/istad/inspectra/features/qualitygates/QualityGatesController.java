package co.istad.inspectra.features.qualitygates;

import co.istad.inspectra.base.BaseRestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quality_gates")
@RequiredArgsConstructor
public class QualityGatesController {

    private final QualityGatesService qualityGatesService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<Object> getQualityGatesByProjectName(String projectName) throws Exception {
        return BaseRestResponse.builder()
                .data(qualityGatesService.getQualityGatesByProjectName(projectName))
                .message("Get quality gates by project name.")
                .build();
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<Object> getAllQualityGates() throws Exception {
        return BaseRestResponse.builder()
                .data(qualityGatesService.getAllQualityGates())
                .message("Get all quality gates.")
                .build();
    }


    @GetMapping("/custom-scan")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<Object> CustomScan(String projectName,String qualityGate) throws Exception {
        return BaseRestResponse.builder()
                .data(qualityGatesService.CustomScan(projectName,qualityGate))
                .message("Custom scan.")
                .build();
    }

}
