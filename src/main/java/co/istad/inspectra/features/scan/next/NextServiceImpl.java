package co.istad.inspectra.features.scan.next;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.domain.Project;
import co.istad.inspectra.features.issue.IssueService;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.features.scan.next.dto.NextScanningRequest;
import co.istad.inspectra.utils.EmailUtil;
import co.istad.inspectra.utils.SonarCustomizeScanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class NextServiceImpl implements NextService{

    @Value("${git.clone_directory}")
    private String clone_dir;

    @Value("${my-app.state}")
    private String myApp;

    private final AppConfig appConfig;

    private final GitConfig gitConfig;

    private final EmailUtil emailUtil;

    private final SonarCustomizeScanUtil sonarCustomizeScanUtil;

    private final ProjectRepository projectRepository;

    private final IssueService issueService;



    @Override
    public String nextScanning(NextScanningRequest nextScanningRequest) {

        if (nextScanningRequest.gitUrl() == null || nextScanningRequest.gitUrl().isEmpty() || nextScanningRequest.projectName() == null || nextScanningRequest.projectName().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Git URL and project name are required");

        }

        Project project = projectRepository.findByProjectName(nextScanningRequest.projectName())

                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (project.getIsUsed()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project is currently in use");

        }

        String currentProjectDir = appConfig.getProjectAbsolutePath();

        String cloneDirectory = currentProjectDir + clone_dir;

        // Clone the repository
        String fileName = gitConfig.gitClone(nextScanningRequest.gitUrl(), nextScanningRequest.branch(), cloneDirectory,"", "next-");

        try {

            if(myApp.equals("dev")) {

                sonarCustomizeScanUtil.getScanLocal1(nextScanningRequest.projectName(), cloneDirectory, fileName, nextScanningRequest.issueTypes(), nextScanningRequest.excludePaths());

            } else {

                sonarCustomizeScanUtil.getProjectScanInProduction(nextScanningRequest.projectName(), cloneDirectory, fileName);

            }

            project.setIsUsed(true);

            projectRepository.save(project);

            //send message when scanning has done


        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Next Project scan failed "+ e);


        }

        return fileName;
    }


}
