package co.istad.inspectra.features.scan.next;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.domain.Project;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.features.scan.ScanServiceImpl;
import co.istad.inspectra.features.scan.dto.ScanningRequestDto;
import co.istad.inspectra.features.scan.next.dto.NextScanningRequest;
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

    private final SonarCustomizeScanUtil sonarCustomizeScanUtil;

    private final ProjectRepository projectRepository;



    @Override
    public String nextScanning(ScanningRequestDto scanningRequestDto) {

        if (scanningRequestDto.gitUrl() == null || scanningRequestDto.gitUrl().isEmpty() || scanningRequestDto.projectName() == null || scanningRequestDto.projectName().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Git URL and project name are required");

        }

        Project project = projectRepository.findByProjectName(scanningRequestDto.projectName())

                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (project.getIsUsed()) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project is currently in use");

        }

        String currentProjectDir = appConfig.getProjectAbsolutePath();

        String cloneDirectory = currentProjectDir + clone_dir;

        // Clone the repository
        String fileName = gitConfig.gitClone(scanningRequestDto.gitUrl(), scanningRequestDto.branch(), cloneDirectory,"", "next-");

        try {

            ScanServiceImpl.checkMyapp(project, cloneDirectory, fileName, myApp, sonarCustomizeScanUtil, scanningRequestDto.projectName(), scanningRequestDto.issueTypes(), scanningRequestDto.includePaths(), projectRepository);

            //send message when scanning has done

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Next Project scan failed "+ e);


        }

        return fileName;
    }


}
