package co.istad.inspectra.features.scanning.fastapi;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.scanning.dto.ScanningRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fastapi")
@RequiredArgsConstructor

public class FastApiController {


    private final FastApiService fastApiService;


    @Operation(summary = "Scan FastApi")
    @PostMapping("/scan")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> scanFastApi(@Valid @RequestBody ScanningRequestDto scanningRequestDto)
    {

        return BaseRestResponse.<String>builder()
                .data(fastApiService.scanFastApi(scanningRequestDto))
                .message("Scanning FastApi successfully")
                .build();


    }

}
