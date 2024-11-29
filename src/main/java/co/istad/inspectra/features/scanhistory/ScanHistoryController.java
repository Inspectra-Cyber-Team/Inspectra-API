package co.istad.inspectra.features.scanhistory;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.scanhistory.dto.ScanHistoryResponseDto;
import co.istad.inspectra.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scan_history")
@RequiredArgsConstructor

public class ScanHistoryController {

    private final ScanHistoryService scanHistoryService;

    @Operation(summary = "Get Scan Histories")
    @GetMapping
    public BaseRestResponse<List<ScanHistoryResponseDto>> getScanHistories(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return BaseRestResponse.<List<ScanHistoryResponseDto>>builder()
                .data(scanHistoryService.getScanHistories(customUserDetails))
                .message("Get Scan Histories successfully")
                .build();
    }

}
