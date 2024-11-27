package co.istad.inspectra.features.scanning.spring;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.DetectSpringBuildTool;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.domain.Project;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.utils.SonarCustomizeScanSpring;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SpringBootServiceImpl implements SpringBootService{


    @Value("${git.clone_directory}")
    private String clone_dir;

    private final GitConfig gitConfig;

    private final AppConfig appConfig;

    private final DetectSpringBuildTool detectSpringBuildTool;

    private final ProjectRepository projectRepository;

    private final SonarCustomizeScanSpring sonarCustomizeScanSpring;

    @Override
    public String springBootScanning(String gitUrl, String branch, String projectName) throws Exception {

        Project project = projectRepository.findByProjectName(projectName)

                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (project.getIsUsed())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project is already in use");
        }


        String currentProjectDir = appConfig.getProjectAbsolutePath();

        String clone_direct = currentProjectDir + clone_dir;

        String fileName = gitConfig.gitClone(gitUrl, branch, clone_direct,"", "spring-boot-");

        if(checkForBuildFiles(clone_direct + fileName)){

            System.out.println("Build files not found immediately. Waiting for 50 seconds ...");
            TimeUnit.SECONDS.sleep(50);

            if(checkForBuildFiles(clone_direct + fileName)){
                throw new Exception("No build.gradle or pom.xml found in the cloned repository after waiting.");
            }
        }

        String buildTool = detectSpringBuildTool.detect(clone_direct + fileName);

        if(buildTool.equalsIgnoreCase("Maven")) {

            sonarCustomizeScanSpring.scanForMaven(clone_direct, fileName, projectName);
            project.setIsUsed(true);
            projectRepository.save(project);

        } else if(buildTool.equalsIgnoreCase("Gradle")) {

            sonarCustomizeScanSpring.scanForGradle(clone_direct, fileName, projectName);
            project.setIsUsed(true);
            projectRepository.save(project);

        }

        return fileName;
    }

    @Override
    public boolean checkForBuildFiles(String projectPath) {

        return !Files.exists(Paths.get(projectPath, "build.gradle")) &&
                !Files.exists(Paths.get(projectPath, "pom.xml"));

    }
}
