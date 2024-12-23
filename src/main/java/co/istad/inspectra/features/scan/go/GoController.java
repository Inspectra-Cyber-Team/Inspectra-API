package co.istad.inspectra.features.scan.go;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.scan.dto.ScanningRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/scans/go")
@RequiredArgsConstructor

public class GoController {

    private final GoService goService;

    @Operation(summary = "Scan Go")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> scanFastApi(@Valid @RequestBody ScanningRequestDto scanningRequestDto)
    {

        return BaseRestResponse.<String>builder()
                .data(goService.scanGo(scanningRequestDto))
                .message("Scanning Go successfully")
                .build();


    }



}
