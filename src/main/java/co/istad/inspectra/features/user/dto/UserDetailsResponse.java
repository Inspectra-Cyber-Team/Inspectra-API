package co.istad.inspectra.features.user.dto;

import co.istad.inspectra.features.blog.dto.BlogResponseDto;
import co.istad.inspectra.features.project.dto.ProjectResponse;
import co.istad.inspectra.features.report.dto.BlogResponse;
import co.istad.inspectra.features.role.dto.RoleResponse;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record UserDetailsResponse(
        String uuid,
        String firstName,
        String lastName,
        String name,
        String email,
        String profile,
        String bio,
        String createdAt,

        Boolean isActive,

        String lastModifiedAt,
        Boolean isVerified,
        Boolean isDeleted,
        Set<RoleResponse> roles,
        List<BlogResponseDto> blog,
        List<ProjectResponse> project
) {
}
