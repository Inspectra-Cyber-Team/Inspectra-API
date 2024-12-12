package co.istad.inspectra.features.git;

import co.istad.inspectra.config.AppConfig;
import co.istad.inspectra.config.GitConfig;
import co.istad.inspectra.features.git.dto.GitBranchResponseDto;
import co.istad.inspectra.features.git.dto.GitRepositoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/gits")
@RequiredArgsConstructor
public class GitController {

    private final GitService gitService;

    private final GitConfig gitConfig;

    @Value("${git.clone_directory}")
    private String clone_dir;

    private final AppConfig appConfig;

    @GetMapping("/repos/{username}")
    @Operation(summary = "Get all repositories by user and only public repositories")
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
    public Flux<GitRepositoryResponse> getGitRepository(@PathVariable String username, @PathVariable String projectName)
    {
        return gitService.getRepositories(username, projectName);

    }



    @GetMapping("/repos")
    @Operation(summary = "Get all repositories by user and only public repositories")
    @ResponseStatus(HttpStatus.OK)
    public Flux<GitRepositoryResponse> getAllRepo(@RequestParam String accessToken)
    {
        return gitService.getAllRepo(accessToken);
    }


    @Operation(summary = "Get all branches by user and project name")
    @GetMapping("/branches/{username}/{projectName}")
    @ResponseStatus(HttpStatus.OK)
    public Flux<GitBranchResponseDto> getBranches(@PathVariable String username, @PathVariable String projectName)
    {
        return gitService.getBranches(username, projectName);

    }


    @Operation(summary = "Get all branches by git url")
    @GetMapping("/branches")
    @ResponseStatus(HttpStatus.OK)
    public Flux<GitBranchResponseDto> getBranchesNameByGitUrl(@RequestParam String gitUrl)
    {

        return gitService.getBranches1(gitUrl);

    }


    @Operation(summary = "Get all file and directories by user and project name")
    @GetMapping("/list_files")
    @ResponseStatus(HttpStatus.OK)
    public Map<String,Object> listFilesInRepository (@RequestParam String gitUrl, @RequestParam String branch) throws IOException, GitAPIException {


        String cloneDirectory = clone_dir;

        String fileName = gitConfig.gitClone(gitUrl, branch, cloneDirectory,"", "");

        return gitConfig.listFilesInRepository(cloneDirectory, fileName);

    }



    @Operation(summary = "Clone a repository")
    @PostMapping("/clone")
    public String cloneRepository(@RequestParam String url, @RequestParam String branch) throws GitAPIException {

        String currentProjectDir = appConfig.getProjectAbsolutePath();

        String cloneDirectory = currentProjectDir + clone_dir;

        return gitConfig.gitClone(url, branch, cloneDirectory,"", "");


    }


}
