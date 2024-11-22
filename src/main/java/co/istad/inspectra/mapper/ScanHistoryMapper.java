package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.ScanHistory;
import co.istad.inspectra.features.scanhistory.dto.ScanHistoryResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScanHistoryMapper {

    ScanHistoryResponseDto toScanHistoryResponseDto(ScanHistory scanHistory);

}
