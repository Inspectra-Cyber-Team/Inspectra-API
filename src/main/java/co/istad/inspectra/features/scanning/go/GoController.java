package co.istad.inspectra.features.scanning.go;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.scanning.dto.ScanningRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/go")
@RequiredArgsConstructor

public class GoController {

    private final GoService goService;

    @Operation(summary = "Scan Go")
    @PostMapping("/scan")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> scanFastApi(@Valid @RequestBody ScanningRequestDto scanningRequestDto)
    {

        return BaseRestResponse.<String>builder()
                .data(goService.scanGo(scanningRequestDto))
                .message("Scanning FastApi successfully")
                .build();


    }



}
