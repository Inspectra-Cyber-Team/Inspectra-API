package co.istad.inspectra.features.project_analy;

import co.istad.inspectra.features.project_analy.dto.LinesResponse;
import reactor.core.publisher.Flux;

public interface ProjectAnalyzeService {

    Object analyzeProject(String projectUuid) throws Exception;

    Object duplicateProject(String projectKey) throws Exception;

    Object getMeasures(String projectKey) throws Exception;

    Object getNumberLines(String projectKey) throws Exception;

    Object getProjectOverview(String projectKey) throws Exception;

    Flux<LinesResponse> getLines(String projectKey) throws Exception;

}
