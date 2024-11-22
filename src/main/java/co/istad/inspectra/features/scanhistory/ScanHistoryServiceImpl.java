package co.istad.inspectra.features.scanhistory;

import co.istad.inspectra.domain.ScanHistory;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.scanhistory.dto.ScanHistoryResponseDto;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.mapper.ScanHistoryMapper;
import co.istad.inspectra.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScanHistoryServiceImpl implements ScanHistoryService {

    private final ScanHistoryRepository scanHistoryRepository;

    private final UserRepository userRepository;

    private final ScanHistoryMapper scanHistoryMapper;

    public List<ScanHistoryResponseDto> getScanHistories(@AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (customUserDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }

        String email = customUserDetails.getUsername();

        User user = userRepository.findUsersByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<ScanHistory> scanHistories = scanHistoryRepository.findByUserUuid(user.getUuid());



        return scanHistories.stream()
                .map(scanHistoryMapper::toScanHistoryResponseDto)
                .toList();
    }
}
