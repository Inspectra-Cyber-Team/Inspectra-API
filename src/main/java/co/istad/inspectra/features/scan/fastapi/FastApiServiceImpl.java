package co.istad.inspectra.features.scan.fastapi;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.domain.Project;
import co.istad.inspectra.domain.ScanHistory;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.features.scanhistory.ScanHistoryRepository;
import co.istad.inspectra.features.scan.dto.ScanningRequestDto;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.utils.EmailUtil;
import co.istad.inspectra.utils.SonarCustomizeScanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.UUID;


@Service
@RequiredArgsConstructor

public class FastApiServiceImpl implements FastApiService {



    @Value("${git.clone_directory}")
    private String clone_dir;

    @Value("${my-app.state}")
    private String myApp;

    private final AppConfig appConfig;

    private final EmailUtil emailUtil;

    private final GitConfig gitConfig;

    private final UserRepository userRepository;

    private final ScanHistoryRepository scanHistoryRepository;

    private final SonarCustomizeScanUtil sonarCustomizeScanUtil;

    private final ProjectRepository projectRepository;

    @Override
    public String scanFastApi(ScanningRequestDto scanningRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if authentication is null or not authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        // Get principal from authentication
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        // Get email from UserDetails
        UserDetails userDetails = (UserDetails) principal;

        String email = userDetails.getUsername();

        User user = userRepository.findUsersByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Project project = projectRepository.findByProjectName(scanningRequestDto.projectName())

                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        ScanHistory scanHistory = new ScanHistory();
        scanHistory.setUuid(UUID.randomUUID().toString());
        scanHistory.setProjectName(scanningRequestDto.projectName());
        scanHistory.setUser(user);

        scanHistoryRepository.save(scanHistory);


        String currentProjectDir = appConfig.getProjectAbsolutePath();

        String cloneDirectory = currentProjectDir + clone_dir;

        // Clone the repository
        String fileName = gitConfig.gitClone(
                scanningRequestDto.gitUrl(),
                scanningRequestDto.branch(),
                cloneDirectory,
                "",
                "fastapi-"
        );


        try {
            // Execute the SonarQube scan
            if(myApp.equals("dev")) {

                sonarCustomizeScanUtil.getScanLocal(project.getProjectName(), cloneDirectory, fileName);

            }else {

                sonarCustomizeScanUtil.getProjectScanInProduction(project.getProjectName(), cloneDirectory, fileName);
            }

            project.setIsUsed(true);

            projectRepository.save(project);

            // Notify the user of successful completion
            emailUtil.sendScanMessage(
                    "lyhou282@gmail.com",
                    "SonarQube scan for project '" + scanningRequestDto.projectName() + "' completed successfully."
            );

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "FastApi project scan failed '" + scanningRequestDto.projectName() + "'.", e
            );
        }

        return fileName;
    }




}
