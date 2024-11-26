package co.istad.inspectra.mapper;


import co.istad.inspectra.domain.role.Role;
import co.istad.inspectra.features.userrole.dto.UserRoleRequest;
import co.istad.inspectra.features.userrole.dto.UserRoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "uuid", ignore = true)
    Role toRole(UserRoleRequest userRoleRequest);

    UserRoleResponse toUserRoleResponse(Role role);


}
