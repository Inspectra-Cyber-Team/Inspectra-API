package co.istad.inspectra.features.scan.react;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.domain.Project;
import co.istad.inspectra.features.issue.IssueService;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.features.scan.dto.ScanningRequestDto;
import co.istad.inspectra.utils.EmailUtil;
import co.istad.inspectra.utils.SonarCustomizeScanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor

public class ReactServiceImpl implements ReactService {

    private final GitConfig gitConfig;

    @Value("${git.clone_directory}")
    private String clone_dir;

    @Value("${my-app.state}")
    private String myApp;

    private final AppConfig appConfig;

    private final EmailUtil emailUtil;

    private final ProjectRepository projectRepository;

    private final SonarCustomizeScanUtil sonarCustomizeScanUtil;

    private final IssueService issueService;


    @Override
    public String scanningReact(ScanningRequestDto scanningRequestDto) {

        String currentProjectDir = appConfig.getProjectAbsolutePath();

        String cloneDirectory = currentProjectDir + clone_dir;

        String fileName = gitConfig.gitClone(scanningRequestDto.gitUrl(), scanningRequestDto.branch(), cloneDirectory,"", "react-");

        Project project = projectRepository.findByProjectName(scanningRequestDto.projectName())

                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (project.getIsUsed())
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project is currently in use");
        }


        try {

            if (myApp.equals("dev"))
            {
                sonarCustomizeScanUtil.getScanLocal(project.getProjectName(), cloneDirectory, fileName, scanningRequestDto.issueTypes(), scanningRequestDto.includePaths());

            } else {

               sonarCustomizeScanUtil.getProjectScanInProduction(project.getProjectName(), cloneDirectory, fileName, scanningRequestDto.issueTypes(), scanningRequestDto.includePaths());

            }

            project.setIsUsed(true);

            projectRepository.save(project);

            //send message when scanning has done



        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"SonarQube scan failed", e);

        }

        return fileName;

    }







}
