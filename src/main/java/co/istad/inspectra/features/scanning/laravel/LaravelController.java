package co.istad.inspectra.features.scanning.laravel;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.scanning.dto.ScanningRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/laravel")
@RequiredArgsConstructor

public class LaravelController {

    private final LaravelService laravelService;

    @Operation(summary = "Scan Laravel project")
    @PostMapping("/scan")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> scanLaravelProject(@Valid @RequestBody ScanningRequestDto scanningRequestDto) {
        return BaseRestResponse.<String>builder()
                .data(laravelService.scanLaravelProject(scanningRequestDto))
                .message("Laravel project scanned successfully")
                .build();
    }

}
