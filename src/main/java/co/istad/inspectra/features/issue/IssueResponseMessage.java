package co.istad.inspectra.features.issue;

import lombok.Builder;

@Builder
public record IssueResponseMessage(

        String key,
        String rule,
        String component,
        String message

) {
}
