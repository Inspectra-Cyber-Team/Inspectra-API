package co.istad.inspectra.features.report;

import co.istad.inspectra.features.report.dto.ReportRequest;
import co.istad.inspectra.features.report.dto.ReportResponse;
import co.istad.inspectra.features.report.dto.ReportResponseDetails;
import co.istad.inspectra.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

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

    /**
     * getAllReport
     * @param page int
     * @param size int
     * @return Page<ReportResponse>
     */

    Page<ReportResponse> getAllReport(int page, int size);

    /**
     * getReportByUuid
     * @param uuid String
     * @return ReportResponseDetails
     */

    ReportResponseDetails getReportByUuid(String uuid);


    /**
     * deleteReport
     * @param uuid String
     */
    void deleteReport(String uuid);


    /**
     * getReportByBlogUuid
     * @param blogUuid String
     * @return Page<ReportResponse>
     */
    Page<ReportResponse> getReportByBlogUuid(String blogUuid);





}
