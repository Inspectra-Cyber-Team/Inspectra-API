package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Project;
import co.istad.inspectra.features.project.dto.ProjectResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProjectMapper {

     ProjectResponse mapToProjectResponse(Project project);


}
