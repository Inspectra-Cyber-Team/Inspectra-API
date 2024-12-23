package co.istad.inspectra.features.scan;

import co.istad.inspectra.base.SonaResponse;
import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.DetectSpringBuildTool;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.domain.Project;
import co.istad.inspectra.features.issue.IssueService;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.features.scan.dto.ScanForNonUserRequest;
import co.istad.inspectra.features.scan.dto.ScanningRequestDto;
import co.istad.inspectra.utils.EmailUtil;
import co.istad.inspectra.utils.SonarCustomizeScanSpringUtil;
import co.istad.inspectra.utils.SonarCustomizeScanUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor

public class ScanServiceImpl implements ScanService {

    @Value("${git.clone_directory}")
    private String clone_dir;

    @Value("${my-app.state}")
    private String myApp;


    @Value("${sonar.url}")
    private String sonarUrl;

    @Value("${sonar.token}")
    private String sonarToken;

    private final ProjectRepository projectRepository;

    private final AppConfig appConfig;

    private final GitConfig gitConfig;

    private final SonarCustomizeScanUtil sonarCustomizeScanUtil;

    private final DetectSpringBuildTool detectSpringBuildTool;

    private final SonarCustomizeScanSpringUtil sonarCustomizeScanSpringUtil;

    private final SonaResponse sonaResponse;

    private final EmailUtil emailUtil;

    private final IssueService issueService;

    private static final int MAX_SCAN_ATTEMPTS = 3;

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


        String fileName ;

        if (scanningRequestDto.accessToken() == null)
        {
            fileName = gitConfig.gitClone(scanningRequestDto.gitUrl(), scanningRequestDto.branch(), cloneDirectory, "", "project-");
        } else {
            fileName = gitConfig.gitClone(scanningRequestDto.gitUrl(), scanningRequestDto.branch(), cloneDirectory, scanningRequestDto.accessToken(), "project-");
        }

        String buildTool = detectSpringBuildTool.detect(cloneDirectory + fileName);

        if (buildTool.equalsIgnoreCase("Maven")) {

            sonarCustomizeScanSpringUtil.scanForMaven(cloneDirectory ,fileName, scanningRequestDto.projectName());

            project.setIsUsed(true);

            projectRepository.save(project);

        } else if (buildTool.equalsIgnoreCase("Gradle")) {

            sonarCustomizeScanSpringUtil.scanForGradle(cloneDirectory, fileName, scanningRequestDto.projectName());

            project.setIsUsed(true);

            projectRepository.save(project);

        } else {

            try {

                checkMyapp(project, cloneDirectory, fileName, myApp, sonarCustomizeScanUtil, scanningRequestDto.projectName(), scanningRequestDto.issueTypes(), scanningRequestDto.includePaths(), projectRepository);
                // send message when scanning has completed here

                var issue = issueService.getIssueByProjectFilter(scanningRequestDto.projectName(), 1, 10).collectList().block();
                // send message when scanning has completed here
                emailUtil.sendScanMessage(project.getUser().getEmail(),project.getUser().getName(),issue,project.getProjectName());


            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fallback scan failed: " + e.getMessage());
            }
        }

        return fileName;
    }

    @Override
    public String scanForNonUser(ScanForNonUserRequest scanForNonUserRequest) throws Exception {

        int scanCount = scanForNonUserRequest.countScan();

        if (scanCount > MAX_SCAN_ATTEMPTS) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum scan attempts reached");

        }

        Project project = new Project();

        project.setUuid(UUID.randomUUID().toString());
        project.setUser(null);
        project.setProjectName("non-user-" + project.getUuid());
        project.setIsUsed(false);


        projectRepository.save(project);

        String url = sonarUrl + "/api/projects/create";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set("Authorization", "Bearer " + sonarToken);

        String body = "project=" + project.getProjectName() +
                "&name=" + project.getProjectName() +
                "&visibility=" + "public";

         sonaResponse.responseFromSonarAPI(url, body, httpHeaders, HttpMethod.POST);

        String currentProjectDir = appConfig.getProjectAbsolutePath();

        String cloneDirectory = currentProjectDir + clone_dir;

        String fileName ;

        if (scanForNonUserRequest.accessToken() == null)
        {
            fileName = gitConfig.gitClone(scanForNonUserRequest.gitUrl(), scanForNonUserRequest.branch(), cloneDirectory, "", "project-");
        } else {
            fileName = gitConfig.gitClone(scanForNonUserRequest.gitUrl(), scanForNonUserRequest.branch(), cloneDirectory, scanForNonUserRequest.accessToken(), "project-");
        }

        String buildTool = detectSpringBuildTool.detect(cloneDirectory + fileName);

        if (buildTool.equalsIgnoreCase("Maven")) {

            sonarCustomizeScanSpringUtil.scanForMaven(cloneDirectory, fileName, project.getProjectName());

            project.setIsUsed(true);

            projectRepository.save(project);

        } else if (buildTool.equalsIgnoreCase("Gradle")) {

            sonarCustomizeScanSpringUtil.scanForGradle(cloneDirectory, fileName, project.getProjectName());

            project.setIsUsed(true);

            projectRepository.save(project);

        } else {

            try {

                checkMyapp(project, cloneDirectory, fileName, myApp, sonarCustomizeScanUtil, project.getProjectName(), scanForNonUserRequest.issueTypes(), scanForNonUserRequest.includePaths(), projectRepository);


            } catch (Exception e) {

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fallback scan failed: " + e.getMessage());

            }
        }

        return project.getProjectName();

    }

    public static void checkMyapp(Project project, String cloneDirectory, String fileName, String myApp, SonarCustomizeScanUtil sonarCustomizeScanUtil, String projectName, List<String> strings, List<String> strings2, ProjectRepository projectRepository) throws MessagingException, IOException, InterruptedException {

        if (myApp.equals("dev")) {

            sonarCustomizeScanUtil.getScanLocal(projectName, cloneDirectory, fileName, strings, strings2);

        } else {

            sonarCustomizeScanUtil.getProjectScanInProduction(projectName, cloneDirectory, fileName, strings, strings2);

        }

        project.setIsUsed(true);

        projectRepository.save(project);
    }


}
