package co.istad.inspectra.utils;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor

public class SonarCustomizeScanUtil {

    @Value("${sonar.token}")
    private String sonarLoginToken;

    @Value("${sonar.url}")
    private String sonarHostUrl;

    private final EmailUtil emailUtil;

    public void getScanLocal(String projectName, String cloneDirectory, String fileName) throws MessagingException, IOException, InterruptedException {

        String projectPath = getProjectPath(cloneDirectory, fileName);

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

        scanProject(command);
    }

    public void getProjectScanInProduction(String projectName, String cloneDirectory, String fileName) throws MessagingException, IOException, InterruptedException {

        String projectPath = getProjectPath(cloneDirectory, fileName);

        List<String> command = new ArrayList<>();
        command.add("sonar-scanner");
        command.add("-Dsonar.host.url=" + sonarHostUrl);
        command.add("-Dsonar.token=" + sonarLoginToken);
        command.add("-Dsonar.projectKey=" + projectName);
        command.add("-Dsonar.projectName=" + projectName);
        command.add("-Dsonar.sources=" + projectPath);
        command.add("-Dsonar.verbose=true");
        command.add("-X");


       scanProject(command);

    }

    public void scanProject(List<String> command) throws InterruptedException, IOException, MessagingException {

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

    public String getProjectPath(String cloneDirectory, String fileName) {

        return cloneDirectory + fileName;

    }


}
