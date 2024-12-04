package co.istad.inspectra.features.scan;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.DetectSpringBuildTool;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.domain.Project;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.features.scan.dto.ScanningRequestDto;
import co.istad.inspectra.utils.SonarCustomizeScanSpringUtil;
import co.istad.inspectra.utils.SonarCustomizeScanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor

public class ScanServiceImpl implements ScanService {

    @Value("${git.clone_directory}")
    private String clone_dir;

    @Value("${my-app.state}")
    private String myApp;

    private final ProjectRepository projectRepository;

    private final AppConfig appConfig;

    private final GitConfig gitConfig;

    private final SonarCustomizeScanUtil sonarCustomizeScanUtil;

    private final DetectSpringBuildTool detectSpringBuildTool;

    private final SonarCustomizeScanSpringUtil sonarCustomizeScanSpringUtil;

    @Override
    public String scanProject(ScanningRequestDto scanningRequestDto) throws Exception {

        // Fetch the project
        Project project = projectRepository.findByProjectName(scanningRequestDto.projectName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        // Ensure project is not already in use
        if (project.getIsUsed()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project is currently in use");
        }


        String currentProjectDir = appConfig.getProjectAbsolutePath();

        String cloneDirectory = currentProjectDir + clone_dir;


        String fileName = gitConfig.gitClone(scanningRequestDto.gitUrl(), scanningRequestDto.branch(), cloneDirectory, "", "project-");

        // Detect the build tool
        String buildTool = detectSpringBuildTool.detect(cloneDirectory + fileName);


        // Perform scanning based on the build tool
        if (buildTool.equalsIgnoreCase("Maven")) {

            sonarCustomizeScanSpringUtil.scanForMaven(cloneDirectory ,fileName, scanningRequestDto.projectName());

            project.setIsUsed(true);

            projectRepository.save(project);

        } else if (buildTool.equalsIgnoreCase("Gradle")) {

            sonarCustomizeScanSpringUtil.scanForGradle(cloneDirectory, fileName, scanningRequestDto.projectName());

            project.setIsUsed(true);

            projectRepository.save(project);

        } else {
            // Fallback if no recognized build tool is found
            try {

                if (myApp.equals("dev")) {

                    sonarCustomizeScanUtil.getScanLocal(scanningRequestDto.projectName(), cloneDirectory, fileName, scanningRequestDto.issueTypes(), scanningRequestDto.includePaths());

                } else {

                    sonarCustomizeScanUtil.getProjectScanInProduction(scanningRequestDto.projectName(), cloneDirectory, fileName, scanningRequestDto.issueTypes(), scanningRequestDto.includePaths());

                }

                project.setIsUsed(true);

                projectRepository.save(project);

                // send message when scanning has completed here


            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fallback scan failed: " + e.getMessage());
            }
        }

        return fileName;
    }


    @Override
    public boolean checkForBuildFiles(String projectPath) {

        return !Files.exists(Paths.get(projectPath, "build.gradle")) &&
                !Files.exists(Paths.get(projectPath, "pom.xml"));

    }
}
