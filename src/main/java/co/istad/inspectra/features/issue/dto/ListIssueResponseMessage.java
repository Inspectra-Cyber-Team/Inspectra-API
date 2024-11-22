package co.istad.inspectra.features.issue.dto;

import co.istad.inspectra.features.issue.IssueResponseMessage;
import lombok.Builder;

import java.util.List;

@Builder
public record ListIssueResponseMessage(

        List<IssueResponseMessage> issues
) {
}
