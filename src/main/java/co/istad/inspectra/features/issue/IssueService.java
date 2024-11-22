package co.istad.inspectra.features.issue;

import co.istad.inspectra.features.issue.dto.IssuesResponse;
import co.istad.inspectra.features.issue.dto.IssuesWrapperResponse;
import co.istad.inspectra.features.issue.dto.ListIssueResponseMessage;
import reactor.core.publisher.Flux;

public interface IssueService {

    Object getIssueByProjectName(String projectName) throws Exception;

    Object getIssueByIssueKey(String issueKey, String ruleKey) throws Exception;

    Object getIssueByProjectFilter(String projectName) throws Exception;

    Object getIssueDetails() throws Exception;

    Object getCodeQualityIssues(String projectName) throws Exception;

    Flux<IssuesResponse> getIssueByProjectNameFlux(String projectName) throws Exception;

    Flux<ListIssueResponseMessage>  getComponentIssuesMessage(String projectName) throws Exception;

}
