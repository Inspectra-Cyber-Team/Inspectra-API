package co.istad.inspectra.utils;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SonarCustomizeScan {

    @Value("${sonar.token}")
    private String sonarUserToken;

    @Value("${sonar.url}")
    private String sonarUrl;

    @Value("${my-app.state}")
    private String myApp;


    private void buildSpringProject(String projectPath, String projectName, String buildTool) throws Exception {

        if ("Maven".equals(buildTool)) {

            buildWithMaven(projectPath, projectName);

        } else if ("Gradle".equals(buildTool)) {

            buildWithGradle(projectPath, projectName);

        } else {

            throw new Exception("Invalid build tool.");

        }
    }

    public  void buildWithGradle(String projectPath, String projectName) throws Exception {

        List<String> dockerBuildCommand = new ArrayList<>();

        if(myApp.equalsIgnoreCase("dev")){
            dockerBuildCommand.add("docker");
            dockerBuildCommand.add("run");
            dockerBuildCommand.add("--rm");
            dockerBuildCommand.add("-v");
            dockerBuildCommand.add(projectPath + projectName + ":/home/gradle/" + projectName);
            dockerBuildCommand.add("gradle");
            dockerBuildCommand.add("gradle");
            dockerBuildCommand.add("-p");
            dockerBuildCommand.add("/home/gradle/" + projectName);
            dockerBuildCommand.add("build");
            dockerBuildCommand.add("--warning-mode");
            dockerBuildCommand.add("all");

            System.out.println("Debug: Docker Gradle command = " + String.join(" ", dockerBuildCommand));

        }else {

            dockerBuildCommand.add("sh");
            dockerBuildCommand.add("-c");
            dockerBuildCommand.add("cd " + projectPath + projectName + " && gradle build --warning-mode all  --scan");

            System.out.println("Debug: Gradle command = " + String.join(" ", dockerBuildCommand));
        }

        runCommand(dockerBuildCommand);

    }

    public  void buildWithMaven(String projectPath, String projectName) throws Exception {
        System.out.println("Debug: Maven build - Project path = " + projectPath);
        System.out.println("Debug: Maven build - Project name = " + projectName);

        List<String> dockerBuildCommand = new ArrayList<>();
        if(myApp.equalsIgnoreCase("dev")){


            dockerBuildCommand.add("docker");
            dockerBuildCommand.add("run");
            dockerBuildCommand.add("--rm");
            dockerBuildCommand.add("-v");
            String volumeMount = projectPath + projectName + ":/home/maven/" + projectName;
            dockerBuildCommand.add(volumeMount);
            dockerBuildCommand.add("maven");
            dockerBuildCommand.add("mvn");
            dockerBuildCommand.add("-f");
            dockerBuildCommand.add("/home/maven/" + projectName + "/pom.xml");
            dockerBuildCommand.add("clean");
            dockerBuildCommand.add("install");

        }else {
            dockerBuildCommand.add("sh");
            dockerBuildCommand.add("-c");
            dockerBuildCommand.add("cd " + projectPath + projectName + " && mvn clean install -B -e");
        }

        runCommand(dockerBuildCommand);
    }

    public void scanForMaven(String projectPath, String projectName, String sonarProjectKey) throws Exception {
        buildSpringProject(projectPath,projectName,"Maven");
        List<String> command;
        if(myApp.equalsIgnoreCase("dev")){
            command = sonarCLIForMavenOfGradleInLocal("Maven",
                    projectPath+projectName,
                    sonarProjectKey);
        }else {
            command = sonarCLIForMavenOfGradleInProduction("Maven", projectPath+projectName, sonarProjectKey);
        }
        runCommand(command);

    }

    public void scanForGradle(String projectPath,String projectName, String sonarProjectKey) throws Exception{
        buildSpringProject(projectPath,projectName,"Gradle");

        List<String> command;
        if(myApp.equalsIgnoreCase("dev")){
            command = sonarCLIForMavenOfGradleInLocal("Gradle",
                    projectPath+projectName,
                    sonarProjectKey);
        }else {
            command = sonarCLIForMavenOfGradleInProduction("Gradle",
                    projectPath+projectName,
                    sonarProjectKey);
        }
        runCommand(command);


    }

    private List<String> sonarCLIForMavenOfGradleInLocal(String buildToolName,
                                                              String projectPath,
                                                              String sonarProjectKey){

        String binariesPath = buildToolName.equalsIgnoreCase("Maven")
                ? "target/classes,target"
                : "build/classes/java/main";

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
        command.add("-Dsonar.projectKey=" + sonarProjectKey);
        command.add("-Dsonar.projectName=" + sonarProjectKey);
        command.add("-Dsonar.host.url=" + sonarUrl);
        command.add("-Dsonar.token=" + sonarUserToken);
        command.add("-Dsonar.sources=.");
        command.add("-Dsonar.java.binaries=" + binariesPath);

        return command;
    }
    private List<String> sonarCLIForMavenOfGradleInProduction(String buildToolName,
                                                              String projectPath,
                                                              String sonarProjectKey){
        // Determine the binaries directory based on the build tool
        String binariesPath = buildToolName.equalsIgnoreCase("Maven")
                ? "/target/classes"
                : "/build/classes/java/main";
        List<String> command = new ArrayList<>();
        command.add("sonar-scanner");
        command.add("-Dsonar.host.url=" + sonarUrl);
        command.add("-Dsonar.token=" + sonarUserToken);
        command.add("-Dsonar.projectKey=" + sonarProjectKey);
        command.add("-Dsonar.projectName=" + sonarProjectKey);
        command.add("-Dsonar.sources=" + projectPath);
        command.add("-Dsonar.java.binaries=" + projectPath + binariesPath);
        command.add("-Dsonar.verbose=true");
        command.add("-X");
        return command;
    }
    private void runCommand(List<String> command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("OUTPUT: " + line);
        }

        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            System.err.println("ERROR: " + errorLine);
        }
        int exitCode = process.waitFor();
        System.out.println("Command executed with exit code: " + exitCode);
        reader.close();
        errorReader.close();
    }
}
