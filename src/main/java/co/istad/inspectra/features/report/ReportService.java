package co.istad.inspectra.features.report;

import co.istad.inspectra.features.report.dto.ReportRequest;
import co.istad.inspectra.features.report.dto.ReportResponse;
import co.istad.inspectra.features.report.dto.ReportResponseDetails;
import co.istad.inspectra.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * ReportService
 * @auth lyhou
 * @see ReportServiceImpl
 */
public interface ReportService {

    /**
     * createReport
     * @param reportRequest ReportRequest
     * @return ReportResponse
     */
    ReportResponse createReport(ReportRequest reportRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails);

    /**
     * getAllReportDetails
     * @param page int
     * @param size int
     * @return Page<ReportResponseDetails>
     */
    Page<ReportResponseDetails> getAllReportDetails(int page, int size);


    Page<ReportResponse> getAllReport(int page, int size);

    ReportResponseDetails getReportByUuid(String uuid);






}
