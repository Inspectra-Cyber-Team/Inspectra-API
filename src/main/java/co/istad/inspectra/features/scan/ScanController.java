package co.istad.inspectra.features.scan;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.scan.dto.ScanForNonUserRequest;
import co.istad.inspectra.features.scan.dto.ScanningRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/scan")

public class ScanController {

    private final ScanService scanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public BaseRestResponse<String> scanProject(@Valid  @RequestBody ScanningRequestDto scanningRequestDto) throws Exception {

        return BaseRestResponse.<String>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(scanService.scanProject(scanningRequestDto))
                .message("Project scanned successfully")
                .build();

    }

    @PostMapping("/non-user")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseRestResponse<String> scanForNonUser(@Valid @RequestBody ScanForNonUserRequest scanForNonUserRequest) throws Exception {

        return BaseRestResponse.<String>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(scanService.scanForNonUser(scanForNonUserRequest))
                .message("Project scanned successfully")
                .build();

    }
}
