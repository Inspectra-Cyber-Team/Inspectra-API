package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Report;
import co.istad.inspectra.features.report.dto.ReportRequest;
import co.istad.inspectra.features.report.dto.ReportResponse;
import co.istad.inspectra.features.report.dto.ReportResponseDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ReportMapper {

    Report mapToReport(ReportRequest reportRequest);

    ReportResponse mapToReportResponse(Report report);

    ReportResponseDetails mapToReportResponseDetails(Report report);



}
