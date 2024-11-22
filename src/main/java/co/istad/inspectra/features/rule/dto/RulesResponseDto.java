package co.istad.inspectra.features.rule.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record RulesResponseDto(
        String key,
        String repo,
        String name,
        String htmlDesc, // Adjusted from the API response field "htmlDesc"
        String severity,
        String status,
        String internalKey,
        boolean template,
        List<String> tags,
        List<String> sysTags,
        String remFnType,
        String remFnGapMultiplier,
        String remFnBaseEffort,
        String defaultRemFnType,
        String defaultRemFnGapMultiplier,
        String defaultRemFnBaseEffort,
        boolean remFnOverloaded,
        String gapDescription,
        String lang,
        String langName,
        String scope,
        boolean isExternal,
        String type,
        String cleanCodeAttributeCategory,
        String cleanCodeAttribute,
        List<DescriptionSectionDTO> descriptionSections,
        List<ImpactDTO> impacts,
        List<ParamDTO> params,
        List<ActiveDTO> actives // Nested list of active profiles
) {}

// Nested DTO for description sections
record DescriptionSectionDTO(String key, String content, ContextDTO context) {}

// Nested DTO for context inside description sections
record ContextDTO(String displayName, String key) {}

// Nested DTO for impacts
record ImpactDTO(String softwareQuality, String severity) {}

// Nested DTO for params
record ParamDTO(String key, String desc, String defaultValue) {}

// Nested DTO for active profiles
record ActiveDTO(
        String qProfile,
        String inherit,
        String severity,
        boolean prioritizedRule,
        List<ParamDTO> params
) {}
