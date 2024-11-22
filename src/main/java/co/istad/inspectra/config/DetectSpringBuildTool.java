package co.istad.inspectra.config;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class DetectSpringBuildTool {
    public String detect(String projectDir){

        Path path = Paths.get(projectDir);

        if (Files.exists(path.resolve("pom.xml"))) {
            return "Maven";
        } else if (Files.exists(path.resolve("build.gradle")) || Files.exists(path.resolve("build.gradle.kts"))) {
            return "Gradle";
        } else {
            return "Unknown";
        }
    }
}