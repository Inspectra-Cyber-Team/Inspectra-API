package co.istad.inspectra.features.scanhistory;

import co.istad.inspectra.features.scanhistory.dto.ScanHistoryResponseDto;
import co.istad.inspectra.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface ScanHistoryService {

    List<ScanHistoryResponseDto> getScanHistories(@AuthenticationPrincipal CustomUserDetails customUserDetails);


}
