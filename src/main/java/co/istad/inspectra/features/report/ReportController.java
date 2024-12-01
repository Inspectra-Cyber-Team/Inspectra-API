package co.istad.inspectra.features.report;

import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.report.dto.ReportRequest;
import co.istad.inspectra.features.report.dto.ReportResponse;
import co.istad.inspectra.features.report.dto.ReportResponseDetails;
import co.istad.inspectra.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor

public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Create blog")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BaseRestResponse<ReportResponse> createReport(@Valid @RequestBody ReportRequest reportRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return BaseRestResponse.<ReportResponse>builder()
                .timestamp(LocalDateTime.now())
                .data(reportService.createReport(reportRequest, customUserDetails))
                .message("Report created successfully")
                .status(HttpStatus.CREATED.value())
                .build();

    }

    @Operation(summary = "get all report details")
    @GetMapping("/details")
    public Page<ReportResponseDetails> getAllReportDetails(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size) {

        return reportService.getAllReportDetails(page, size);

    }

    @Operation(summary = "get all reports")
    @GetMapping
    public Page<ReportResponse> getAllReport(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size) {

        return reportService.getAllReport(page, size);

    }

    @Operation(summary = "get report by uuid")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<ReportResponseDetails> getReportByUuid(@PathVariable String uuid) {

        return BaseRestResponse.<ReportResponseDetails>builder()
                .timestamp(LocalDateTime.now())
                .data(reportService.getReportByUuid(uuid))
                .message("Report retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();

    }


    @Operation(summary = "delete report by uuid")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseRestResponse<ReportResponse> deleteReport(@PathVariable String uuid) {

        reportService.deleteReport(uuid);

        return BaseRestResponse.<ReportResponse>builder()
                .timestamp(LocalDateTime.now())
                .message("Report deleted successfully")
                .status(HttpStatus.NO_CONTENT.value())
                .build();

    }


}
