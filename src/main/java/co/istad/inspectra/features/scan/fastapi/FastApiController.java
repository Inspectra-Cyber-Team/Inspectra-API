package co.istad.inspectra.features.scan.fastapi;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.scan.dto.ScanningRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/scans/fast_api")
@RequiredArgsConstructor

public class FastApiController {


    private final FastApiService fastApiService;


    @Operation(summary = "Scan FastApi")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> scanFastApi(@Valid @RequestBody ScanningRequestDto scanningRequestDto)
    {

        return BaseRestResponse.<String>builder()
                .data(fastApiService.scanFastApi(scanningRequestDto))
                .message("Scanning FastApi successfully")
                .build();


    }

}
