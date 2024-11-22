package co.istad.inspectra.features.git;

import co.istad.inspectra.features.git.dto.GitBranchResponseDto;
import co.istad.inspectra.features.git.dto.GitRepositoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;



@RestController
@RequestMapping("/api/v1/git")
@RequiredArgsConstructor
public class GitController {

    private final GitService gitService;

    @GetMapping("/repos/{username}")
    @Operation(summary = "Get all repositories by user and only public repositories")
    public Flux<GitRepositoryResponse> getAllGitRepository(@PathVariable String username,
                                                           @RequestParam(required = false) String projectName)
    {
//        check if projectName is null or empty then return all repositories
        if(projectName == null || projectName.isEmpty())
        {

            return gitService.getRepositoriesByUser(username,"");

        }

//        else return repositories by project name
        return gitService.getRepositoriesByUser(username,projectName);

    }

    @GetMapping("/repos/{username}/{projectName}")
    public Flux<GitRepositoryResponse> getGitRepository(@PathVariable String username, @PathVariable String projectName)
    {
        return gitService.getRepositories(username, projectName);

    }


    @GetMapping("/repos")
    @Operation(summary = "Get all repositories by user and only public repositories")
    public Flux<GitRepositoryResponse> getAllRepo(@RequestParam String accessToken)
    {
        return gitService.getAllRepo(accessToken);
    }


    @Operation(summary = "Get all branches by user and project name")
    @GetMapping("/branches/{username}/{projectName}")
    public Flux<GitBranchResponseDto> getBranches(@PathVariable String username, @PathVariable String projectName)
    {
        return gitService.getBranches(username, projectName);

    }


    @Operation(summary = "Get all branches by git url")
    @GetMapping("/branches")
    public Flux<GitBranchResponseDto> getBranches1(@RequestParam String gitUrl)
    {
        return gitService.getBranches1(gitUrl);

    }


}
