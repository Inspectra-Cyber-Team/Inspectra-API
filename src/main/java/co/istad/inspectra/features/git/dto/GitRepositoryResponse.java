package co.istad.inspectra.features.git.dto;

import lombok.Builder;

@Builder
public record GitRepositoryResponse(

            String id,
            String full_name,
            String name,
            String description,
            String url,
            String language,
            String size,
            String visibility,
            String watchers,
            String default_branch,
            String created_at,
            String updated_at,
            String pushed_at,
            Owner owner

) {
}
