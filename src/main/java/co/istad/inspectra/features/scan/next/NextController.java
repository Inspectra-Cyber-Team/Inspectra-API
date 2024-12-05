package co.istad.inspectra.features.scan.next;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.scan.dto.ScanningRequestDto;
import co.istad.inspectra.features.scan.next.dto.NextScanningRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/scans/next")
@RequiredArgsConstructor

public class NextController {

    private final NextService nextService;

    @PostMapping
    public BaseRestResponse<Object> nextScanning(@Valid @RequestBody ScanningRequestDto scanningRequestDto) throws Exception {

        return BaseRestResponse.builder()
                .data(nextService.nextScanning(scanningRequestDto))
                .message("Next project has been scanned successfully.")
                .status(200)
                .build();
    }

}
