package co.istad.inspectra.features.user_role;

import co.istad.inspectra.domain.role.EnumRole;
import co.istad.inspectra.domain.role.Role;
import co.istad.inspectra.features.user_role.dto.UserRoleRequest;
import co.istad.inspectra.features.user_role.dto.UserRoleResponse;
import co.istad.inspectra.mapper.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService{

    private final UserRoleRepository userRoleRepository;

    private final UserRoleMapper userRoleMapper;

    @Override
    public List<UserRoleResponse> getAllUserRoles() {

        List<Role> roles = userRoleRepository.findAll();

        return roles.stream()
                .map(userRoleMapper::toUserRoleResponse)
                .toList();

    }

    @Override
    public UserRoleResponse createRole(UserRoleRequest userRoleRequest) {

        return userRoleMapper.toUserRoleResponse(

                userRoleRepository.save(Role.builder()
                        .uuid(UUID.randomUUID().toString())
                        .roleName(EnumRole.valueOf("ROLE_"+userRoleRequest.roleName().toUpperCase()))
                        .build())
        );
    }

    @Override
    public UserRoleResponse updateRole(String uuid, UserRoleRequest userRoleRequest) {

        Role role = getRoleUuid(uuid);

        role.setRoleName(EnumRole.valueOf("ROLE_"+userRoleRequest.roleName().toUpperCase()));

        return userRoleMapper.toUserRoleResponse(userRoleRepository.save(role));
    }

    @Override
    public UserRoleResponse getRole(String uuid) {

        Role role = getRoleUuid(uuid);

        return userRoleMapper.toUserRoleResponse(role);

    }

    @Override
    public void deleteRole(String uuid) {

        Role role = getRoleUuid(uuid);

        userRoleRepository.delete(role);

    }

    public Role getRoleUuid(String uuid) {

        return userRoleRepository.findRoleByUuid(uuid).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

    }
}
