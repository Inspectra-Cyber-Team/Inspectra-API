package co.istad.inspectra.features.source;

import reactor.core.publisher.Flux;

import java.util.HashMap;

/**
 * SourceService is the interface for the SourceServiceImp class
 * @Author : Lyhou
 * @since : 1.0 (2024)
 */
public interface SourceService {

    /**
     * Get code issue by issue key
     * @param issueKey is the key of the issue to get the code issue
     * @return {@link HashMap} of the code issue
     * @throws Exception if the issue key is not found
     *
     * @Author : Lyhou
     *
     */
    HashMap getCodeIssue(String issueKey) throws Exception;

    Flux<Object> getSourceCode(String component) throws Exception;

    Flux<Object> getSourceLinesCode(String componentKey);

}
