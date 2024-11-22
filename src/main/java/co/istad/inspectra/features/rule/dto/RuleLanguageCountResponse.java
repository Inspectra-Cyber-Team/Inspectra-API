package co.istad.inspectra.features.rule.dto;

import lombok.Builder;

@Builder
public record RuleLanguageCountResponse
        (
                String language,
             int count)
{
}
