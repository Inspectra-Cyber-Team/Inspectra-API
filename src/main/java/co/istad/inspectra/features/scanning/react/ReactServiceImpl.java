package co.istad.inspectra.features.scanning.react;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.features.scanning.dto.ScanningRequestDto;
import co.istad.inspectra.utils.EmailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ReactServiceImpl implements ReactService {

    private final GitConfig gitConfig;

    @Value("${git.clone_directory}")
    private String clone_dir;

    @Value("${sonar.url}")
    private String sonarHostUrl;

    @Value("${sonar.token}")
    private String sonarLoginToken;

    private final AppConfig appConfig;

    private final EmailUtil emailUtil;


    @Override
    public String scanningReact(ScanningRequestDto scanningRequestDto) {

        String currentProjectDir = appConfig.getProjectAbsolutePath();

        String cloneDirectory = currentProjectDir + clone_dir;

        String fileName = gitConfig.gitClone(scanningRequestDto.gitUrl(), scanningRequestDto.branch(), cloneDirectory,"");

        List<String> command = getStrings(scanningRequestDto.projectName(), cloneDirectory, fileName);

        try {

            scanProject(command);
            //send message when scanning has done
            emailUtil.sendScanMessage("lyhou282@gmail.com", "SonarQube scan completed");

        } catch (Exception e) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"SonarQube scan failed", e);

        }

        return fileName;

    }



    @NotNull
    private List<String> getStrings(String projectName, String cloneDirectory, String fileName) {

        String projectPath = cloneDirectory + fileName;

        List<String> command = new ArrayList<>();
        command.add("docker");
        command.add("run");
        command.add("--rm");
        command.add("-v");
        command.add(projectPath + ":/usr/src");
        command.add("--network=host");
        command.add("-w");
        command.add("/usr/src");
        command.add("sonarsource/sonar-scanner-cli");

        // SonarQube properties
        command.add("-Dsonar.projectKey=" + projectName);
        command.add("-Dsonar.projectName=" + projectName);
        command.add("-Dsonar.host.url=" + sonarHostUrl);
        command.add("-Dsonar.token=" + sonarLoginToken);
        command.add("-Dsonar.sources=.");
        command.add("-X");

        return command;
    }

    // This method could be part of the class
    private void scanProject(List<String> command) throws InterruptedException, IOException, MessagingException {

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        Process process = processBuilder.start();

        // Capture output
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder output = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append(System.lineSeparator());
            System.out.println("OUTPUT: " + line);
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {

            //sending email when got an error also
            emailUtil.sendScanMessage("lyhou282@gmail.com","SonarQube scan exited with code: " + exitCode + ". Output: " + output);

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"SonarQube scan exited with code: " + exitCode + ". Output: " + output);

        }

    }




}
