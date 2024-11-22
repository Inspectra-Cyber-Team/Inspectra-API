package co.istad.inspectra.features.test1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test1")

public class Test1Controller {

    private final SonarQubeService sonarQubeService;

    @GetMapping("/projects/{projectKey}")
    public Flux<SonarQubeService.ProjectGrade> getProjects(@PathVariable String projectKey) throws Exception {
        return sonarQubeService.getProjectOverview(projectKey);
    }


}
