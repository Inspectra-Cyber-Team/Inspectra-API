package co.istad.inspectra.features.git;

import co.istad.inspectra.features.git.dto.GitBranchResponseDto;
import co.istad.inspectra.features.git.dto.GitRepositoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor

public class GitServiceImpl implements GitService {

    private final WebClient webClient;

    @Override
    public Flux<GitRepositoryResponse> getRepositoriesByUser(String username,String projectName) {

        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(GitRepositoryResponse.class)
                .filter(repository -> repository.name().toLowerCase().contains(projectName.toLowerCase()))
                .onErrorMap(
                        throwable -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "User not found", throwable));


    }

    @Override
    public Flux<GitRepositoryResponse> getRepositories(String username, String projectName) {

        return webClient.get()
                .uri("/repos/{username}/{projectName}", username, projectName)
                .retrieve()
                .bodyToFlux(GitRepositoryResponse.class)
                .onErrorMap(
                        throwable -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Repository not found", throwable));
    }

    @Override
    public Flux<GitRepositoryResponse> getAllRepo(String accessToken) {

        return webClient.get()
                .uri("/user/repos?visibility=all")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToFlux(GitRepositoryResponse.class)
                .onErrorMap(
                        throwable -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Repository not found", throwable));

    }

    @Override
    public Flux<GitBranchResponseDto> getBranches(String username, String projectName) {

        return webClient.get()
                .uri("/repos/{username}/{projectName}/branches", username, projectName)
                .retrieve()
                .bodyToFlux(GitBranchResponseDto.class)
                .onErrorMap(
                        throwable -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, throwable.getMessage()));

    }

    @Override
    public Flux<GitBranchResponseDto> getBranches1(String gitUrl) {

        if (!gitUrl.matches("^(https?://)?(www\\.)?(github\\.com|gitlab\\.com)/.+$")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Only GitHub or GitLab URLs are allowed");
        }

        String gitUsername = gitUrl.split("/")[3];

        String gitProjectName = gitUrl.split("/")[4].replace(".git", "");

        return webClient.get()
                .uri("/repos/{username}/{projectName}/branches", gitUsername, gitProjectName)
                .retrieve()
                .bodyToFlux(GitBranchResponseDto.class)
                .onErrorMap(
                        throwable -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, throwable.getMessage()));

    }


}
