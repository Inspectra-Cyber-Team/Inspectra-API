package co.istad.inspectra.features.issue;

import co.istad.inspectra.features.issue.dto.IssuesResponse;
import co.istad.inspectra.features.issue.dto.IssuesWrapperResponse;
import co.istad.inspectra.features.issue.dto.ListIssueResponseMessage;
import reactor.core.publisher.Flux;

public interface IssueService {

    Object getIssueByProjectName(String projectName,int page,int size) throws Exception;

    Object getIssueByIssueKey(String issueKey, String ruleKey) throws Exception;

    Object getIssueByProjectFilter(String projectName) throws Exception;

    Object getIssueDetails(String issueKey) throws Exception;

    Object getIssue(String projectName,int page,int size) throws Exception;

    Flux<IssuesResponse> getIssueByProjectNameFlux(String projectName) throws Exception;

    Flux<ListIssueResponseMessage>  getComponentIssuesMessage(String projectName) throws Exception;

}
