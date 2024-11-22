package co.istad.inspectra.features.scanning.test;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.features.project.ProjectRepository;
import co.istad.inspectra.features.scanning.test.dto.AnalyzeRequest;
import co.istad.inspectra.utils.EmailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Service
@RequiredArgsConstructor
public class ScanServiceImpl implements ScanService {

    @Value("${sonar.token}")
    private String sonarToken;

    @Value("${sonar.url}")
    private String sonarUrl;

    @Value("${git.clone_directory}")
    private String clone_dir;

    private final GitConfig gitConfig;

    private final AppConfig appConfig;

    private final ProjectRepository projectRepository;

    private final EmailUtil emailUtil;

    @Override
    public ResponseEntity<String> analyzeRepository(AnalyzeRequest request) {


         projectRepository.findByProjectName(request.projectName())

                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));


        if (runSonarAnalysis(request.repoUrl(), request.projectName(), request.branch())) {

            try {
            emailUtil.sendScanMessage("lyhou282@gmail.com", "SonarQube scan completed");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to send otp please try again");
        }

            return ResponseEntity.ok("SonarQube analysis started successfully!");


        } else {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start SonarQube analysis.");

        }


    }

    @Override
    public boolean checkForBuildFiles(String projectPath) {

        return !Files.exists(Paths.get(projectPath, "build.gradle")) &&

                !Files.exists(Paths.get(projectPath, "pom.xml"));

    }

    @Override
    public String sendMessage(String email) {

        try {
            emailUtil.sendScanMessage(email, "SonarQube scan completed");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to send otp please try again");
        }

        return "email sent";
    }



    private boolean runSonarAnalysis(String repoUrl, String projectKey, String branch) {

        String currentProjectDir = appConfig.getProjectAbsolutePath();

        String clone_direct = currentProjectDir + clone_dir;

        String fileName = gitConfig.gitClone(repoUrl, branch, clone_direct,"");

        createSonarProperties(clone_direct + "/" + fileName, projectKey);

        return executeSonarAnalysis(clone_direct + "/" + fileName, projectKey);

    }


    private void createSonarProperties(String projectDir, String projectKey) {

        WritePropertyFile(projectDir, projectKey, sonarUrl, sonarToken);
    }

    static void WritePropertyFile(String projectDir,String projectKey, String sonarUrl, String sonarToken) {

        try (FileWriter writer = new FileWriter(projectDir + "/sonar-project.properties")) {

            writer.write("sonar.projectKey="+ projectKey + "\n");
            writer.write("sonar.projectName="+ projectKey  + "\n");
            writer.write("sonar.host.url=" + sonarUrl + "\n");
            writer.write("sonar.login=" + sonarToken + "\n");

        } catch (IOException e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create sonar-project.properties file");

        }
    }

    private boolean executeSonarAnalysis(String projectDir, String projectKey) {

        return SonarScannerPath(projectDir, projectKey, sonarUrl, sonarToken);
    }

    static boolean SonarScannerPath(String projectDir, String projectKey, String sonarUrl, String sonarToken) {

        String sonarScannerPath = "D:\\CyberSecurity-CSTAD\\Analy\\sonar-scanner-6.1.0.4477-windows-x64\\bin\\sonar-scanner.bat";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    sonarScannerPath,
                    "-Dsonar.projectKey="+ projectKey,
                    "-Dsonar.sources="+projectDir,
                    "-Dsonar.host.url=" + sonarUrl,
                    "-Dsonar.login=" + sonarToken
            );

            processBuilder.directory(new File(projectDir));
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            process.waitFor();

            return process.exitValue() == 0;

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to execute sonar-scanner");

        }
    }

}






