package co.istad.inspectra.features.scan.nestjs;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.scan.dto.ScanningRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/scans/nestjs")
@RequiredArgsConstructor

public class NestjsController {

    private final NestJsService nestJsService;

    @Operation(summary = "Scan a NestJS project")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> scanNestJs(@Valid @RequestBody ScanningRequestDto scanningRequestDto) {

        return BaseRestResponse.<String>builder()
                .data(nestJsService.scanNestJs(scanningRequestDto))
                .message("NestJS project scanned successfully")
                .build();

    }



}
