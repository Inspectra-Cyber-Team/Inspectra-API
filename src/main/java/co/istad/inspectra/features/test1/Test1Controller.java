package co.istad.inspectra.features.test1;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Flux;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/test1")
//
//public class Test1Controller {
//
//    private final SonarQubeService sonarQubeService;
//
//    @GetMapping("/projects/{projectKey}")
//    public Flux<SonarQubeService.ProjectGrade> getProjects(@PathVariable String projectKey) throws Exception {
//        return sonarQubeService.getProjectOverview(projectKey);
//    }
//
//
//}

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test1")

public class Test1Controller {

    @PostMapping("/create")
    public ResponseEntity<String> createProject(HttpServletRequest request) {
        // Retrieve the anonymous token
        String token = (String) request.getAttribute("ANONYMOUS_TOKEN");

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Anonymous token not found");
        }

        // Create a project associated with this token
        return ResponseEntity.ok("Project created for token: " + token);
    }

}