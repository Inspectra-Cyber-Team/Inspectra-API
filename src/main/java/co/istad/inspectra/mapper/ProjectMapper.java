package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.Project;
import co.istad.inspectra.features.project.dto.ProjectResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface ProjectMapper {

     @Named("toProjectResponse")
     ProjectResponse mapToProjectResponse(Project project);


}
