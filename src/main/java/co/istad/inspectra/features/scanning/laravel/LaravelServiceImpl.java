package co.istad.inspectra.features.scanning.laravel;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.domain.Project;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.features.scanning.dto.ScanningRequestDto;
import co.istad.inspectra.utils.EmailUtil;
import co.istad.inspectra.utils.SonarCustomizeScan;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor

public class LaravelServiceImpl implements LaravelService {

    @Value("${git.clone_directory}")
    private String clone_dir;

    @Value("${my-app.state}")
    private String myApp;

    private final AppConfig appConfig;

    private final EmailUtil emailUtil;

    private final GitConfig gitConfig;

    private final ProjectRepository projectRepository;

    private final SonarCustomizeScan sonarCustomizeScan;
    @Override
    public String scanLaravelProject(ScanningRequestDto scanningRequestDto) {

        String currentProjectDir = appConfig.getProjectAbsolutePath();

        String cloneDirectory = currentProjectDir + clone_dir;

        // Clone the NestJS project repository
        String fileName = gitConfig.gitClone(
                scanningRequestDto.gitUrl(),
                scanningRequestDto.branch(),
                cloneDirectory,
                "",
                "laravel-"
        );

        Project project = projectRepository.findByProjectName(scanningRequestDto.projectName())

                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (project.getIsUsed()) {

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project is currently in use");
        }


        try {
            // Execute the scanning process
            if (myApp.equals("dev")) {

                sonarCustomizeScan.getScanLocal(project.getProjectName(), cloneDirectory, fileName);

            } else {

                sonarCustomizeScan.getProjectScanInProduction(project.getProjectName(), cloneDirectory, fileName);

            }

            project.setIsUsed(true);

            projectRepository.save(project);

            // Send notification after successful scanning
            emailUtil.sendScanMessage(
                    "lyhou282@gmail.com",
                    "SonarQube scan for Laravel project '" + scanningRequestDto.projectName() + "' completed successfully."
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "SonarQube scan failed for Laravel project '" + scanningRequestDto.projectName() + "'.", e
            );
        }

        return fileName;
    }






}
