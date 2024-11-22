package co.istad.inspectra.features.scanning.test;
//package co.istad.cyber.features.test;//
//import co.istad.cyber.config.AppConfig;
//import co.istad.cyber.config.GitConfig;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.file.*;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api")
//@RequiredArgsConstructor
//public class SonarQubeController {
//
//    @Value("${sonar.token}")
//    private String sonarToken;
//
//    @Value("${sonar.url}")
//    private String sonarUrl;
//
//    @Value("${git.clone_directory}")
//    private String clone_dir;
//
//    private final GitConfig gitConfig;
//
//    private final AppConfig appConfig;
//
//    @PostMapping("/analyze")
//    public ResponseEntity<String> analyzeRepository(@RequestBody AnalyzeRequest request) {
//        String githubRepoUrl = request.getRepoUrl();
//        String branch = request.getBranch();
//        String projectKey = "project-" + UUID.randomUUID();
//
//        if (runSonarAnalysis(githubRepoUrl, branch)) {
//            return ResponseEntity.ok("SonarQube analysis started successfully!");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start SonarQube analysis.");
//        }
//    }
//
//    private boolean runSonarAnalysis(String repoUrl, String branch) {
//
//        String currentProjectDir = appConfig.getProjectAbsolutePath();
//
//        String clone_direct = currentProjectDir + clone_dir;
//
//        String fileName = gitConfig.gitClone(repoUrl, branch, clone_direct);
//
//        createSonarProperties(fileName, "test", branch);
//
//        // Clean up cloned directory
////            deleteDirectory(clone_direct);
//            return executeSonarAnalysis(clone_direct+"/"+fileName);
//
//    }
//
//
//    private void createSonarProperties(String projectDir, String projectKey, String branch) {
//        System.out.println("Creating sonar-project.properties file..."+projectDir);
//        ScanServiceImpl.WritePropertyFile(projectDir, sonarUrl, sonarToken);
//    }
//
//    private boolean executeSonarAnalysis(String projectDir) {
//
//        return SonarScannerPath(projectDir, sonarUrl, sonarToken);
//    }
//
//    private void deleteDirectory(String directory) {
//        try {
//            Files.walk(Path.of(directory))
//                .map(Path::toFile)
//                .sorted((o1, o2) -> -o1.compareTo(o2))
//                .forEach(File::delete);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

import co.istad.inspectra.features.scanning.test.dto.AnalyzeRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/scan")
@RequiredArgsConstructor
@RestController

public class SonarQubeController {

    private final ScanService scanService;

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeRepository(@RequestBody AnalyzeRequest request) throws MessagingException {

        return scanService.analyzeRepository(request);

    }

    @PostMapping("/send/{email}")
    public String sendMessage(@PathVariable String email) throws MessagingException {
        return scanService.sendMessage(email);
    }


}