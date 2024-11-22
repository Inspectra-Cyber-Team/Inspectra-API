package co.istad.inspectra.features.scanning.nestjs;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.features.scanning.dto.ScanningRequestDto;
import co.istad.inspectra.utils.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
@RequiredArgsConstructor

public class NestJsServiceImpl implements NestJsService {



    @Value("${git.clone_directory}")
    private String clone_dir;

    @Value("${sonar.url}")
    private String sonarHostUrl;

    @Value("${sonar.token}")
    private String sonarLoginToken;

    private final AppConfig appConfig;

    private final EmailUtil emailUtil;

    private final GitConfig gitConfig;

    @Override
    public String scanNestJs(ScanningRequestDto scanningRequestDto) {
        // Base directory for projects
        String currentProjectDir = appConfig.getProjectAbsolutePath();
        String cloneDirectory = currentProjectDir + clone_dir;

        // Clone the NestJS project repository
        String fileName = gitConfig.gitClone(
                scanningRequestDto.gitUrl(),
                scanningRequestDto.branch(),
                cloneDirectory,
                ""
        );

        // Generate the command for scanning the NestJS project
        List<String> command = buildSonarScannerCommand(
                scanningRequestDto.projectName(),
                cloneDirectory,
                fileName
        );

        try {
            // Execute the scanning process
            executeCommand(command);

            // Send notification after successful scanning
            emailUtil.sendScanMessage(
                    "lyhou282@gmail.com",
                    "SonarQube scan for NestJS project '" + scanningRequestDto.projectName() + "' completed successfully."
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "SonarQube scan failed for NestJS project '" + scanningRequestDto.projectName() + "'.",
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
                "-Dsonar.language=ts" // Specify TypeScript as the language for NestJS
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
