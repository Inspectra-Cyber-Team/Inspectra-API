package co.istad.inspectra.features.role;

import co.istad.inspectra.domain.role.EnumRole;
import co.istad.inspectra.domain.role.Role;
import co.istad.inspectra.features.role.dto.RoleRequest;
import co.istad.inspectra.features.role.dto.RoleResponse;
import co.istad.inspectra.mapper.RoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    @Override
    public List<RoleResponse> getAllUserRoles() {

        List<Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(roleMapper::toRoleResponse)
                .toList();

    }

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {

        return roleMapper.toRoleResponse(

                roleRepository.save(Role.builder()
                        .uuid(UUID.randomUUID().toString())
                        .roleName(EnumRole.valueOf("ROLE_"+ roleRequest.roleName().toUpperCase()))
                        .build())
        );
    }

    @Override
    public RoleResponse updateRole(String uuid, RoleRequest roleRequest) {

        Role role = getRoleUuid(uuid);

        role.setRoleName(EnumRole.valueOf("ROLE_"+ roleRequest.roleName().toUpperCase()));

        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Override
    public RoleResponse getRole(String uuid) {

        Role role = getRoleUuid(uuid);

        return roleMapper.toRoleResponse(role);

    }

    @Override
    public void deleteRole(String uuid) {

        Role role = getRoleUuid(uuid);

        roleRepository.delete(role);

    }

    public Role getRoleUuid(String uuid) {

        return roleRepository.findRoleByUuid(uuid).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

    }
}
