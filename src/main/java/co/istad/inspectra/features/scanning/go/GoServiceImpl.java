package co.istad.inspectra.features.scanning.go;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.domain.ScanHistory;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.scanhistory.ScanHistoryRepository;
import co.istad.inspectra.features.scanning.dto.ScanningRequestDto;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.utils.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class GoServiceImpl  implements  GoService{



    @Value("${git.clone_directory}")
    private String clone_dir;

    @Value("${sonar.url}")
    private String sonarHostUrl;

    @Value("${sonar.token}")
    private String sonarLoginToken;

    private final AppConfig appConfig;

    private final EmailUtil emailUtil;

    private final GitConfig gitConfig;

    private final UserRepository userRepository;

    private final ScanHistoryRepository scanHistoryRepository;

    @Override
    public String scanGo(ScanningRequestDto scanningRequestDto) {

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
                ""
        );

        // Generate the SonarQube scan command
        List<String> command = buildSonarScannerCommand(
                scanningRequestDto.projectName(),
                cloneDirectory,
                fileName
        );

        try {
            // Execute the SonarQube scan
            executeCommand(command);

            // Notify the user of successful completion
            emailUtil.sendScanMessage(
                    "lyhou282@gmail.com",
                    "SonarQube scan for project '" + scanningRequestDto.projectName() + "' completed successfully."
            );

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "SonarQube scan failed for project '" + scanningRequestDto.projectName() + "'.",
                    e
            );
        }

        return fileName;
    }

    @NotNull
    private List<String> buildSonarScannerCommand(String projectName, String cloneDirectory, String fileName) {

        String projectPath = cloneDirectory + fileName;

        return List.of(
                "docker", "run", "--rm",
                "-v", projectPath + ":/usr/src",
                "--network=host",
                "-w", "/usr/src",
                "sonarsource/sonar-scanner-cli",
                "-Dsonar.projectKey=" + projectName,
                "-Dsonar.projectName=" + projectName,
                "-Dsonar.host.url=" + sonarHostUrl,
                "-Dsonar.token=" + sonarLoginToken,
                "-Dsonar.sources=.",
                "-X"
        );
    }

    private void executeCommand(List<String> command) throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Log or store the output for debugging
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Command execution failed with exit code: " + exitCode);
        }
    }


}
