package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Report;
import co.istad.inspectra.features.report.dto.ReportRequest;
import co.istad.inspectra.features.report.dto.ReportResponse;
import co.istad.inspectra.features.report.dto.ReportResponseDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface ReportMapper {

    Report mapToReport(ReportRequest reportRequest);

    @Mapping(target = "name", source = "user.name")
    ReportResponse mapToReportResponse(Report report);

    @Mapping(target = "blogUuid", source = "blog.uuid")
    ReportResponseDetails mapToReportResponseDetails(Report report);



}
