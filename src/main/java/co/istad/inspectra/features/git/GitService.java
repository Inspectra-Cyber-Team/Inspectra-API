package co.istad.inspectra.features.git;

import co.istad.inspectra.features.git.dto.GitBranchResponseDto;
import co.istad.inspectra.features.git.dto.GitRepositoryResponse;
import reactor.core.publisher.Flux;

/**
 * GitService
 * @author : lyhou
 * @since : 1.0
 */

public interface GitService {


    /**
     * Get all repositories from a user
     *
     * @return Flux<GitRepositoryResponse>
     *
     * @author : lyhou
     */
    Flux<GitRepositoryResponse> getRepositoriesByUser(String username,String projectName);

    /**
     * Get a repository from a user
     *
     * @return Flux<GitRepositoryResponse>
     * @author : lyhou
     */

    Flux<GitRepositoryResponse> getRepositories(String username, String projectName);


    /**
     * get all repo from a user all public and private repo
     *
     *
     * @param accessToken use for authentication
     * @return {@link Flux<GitRepositoryResponse>}
     */
    Flux<GitRepositoryResponse> getAllRepo (String accessToken);

    /**
     *
     * get all branch from repo git
     * @param username user for get repo
     * @param projectName project name for get repo
     *
     */

    Flux<GitBranchResponseDto> getBranches(String username, String projectName);


    Flux<GitBranchResponseDto> getBranches1(String gitUrl);





}
