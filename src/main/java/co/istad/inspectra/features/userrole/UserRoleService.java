package co.istad.inspectra.features.userrole;

import co.istad.inspectra.features.userrole.dto.UserRoleRequest;
import co.istad.inspectra.features.userrole.dto.UserRoleResponse;

import java.util.List;

/**
 * Service for managing user roles
 * @author : lyhou
 * @since : 1.0 (2024)
 */

public interface UserRoleService {

    /**
     * Get all user roles
     * @author lyhou
     * @since 1.0
     * @return {@link UserRoleResponse} is the response of all user roles
     */
     List <UserRoleResponse> getAllUserRoles();



    /**
     * Create a new role
     * @param userRoleRequest is used to create role in request body
     * @return {@link UserRoleResponse} is the response of the created role
     * @since 1.0
     * @author lyhou
     */
    UserRoleResponse createRole(UserRoleRequest userRoleRequest);


    /**
     * Update a role
     * @param uuid is the unique identifier of the role
     * @param userRoleRequest is the request body to update the role
     * @return {@link UserRoleResponse} is the response of the updated role
     * @since 1.0
     * @author lyhou
     */

    UserRoleResponse updateRole(String uuid, UserRoleRequest userRoleRequest);

    /**
     * Get a role
     * @param uuid is the unique identifier of the role
     * @return {@link UserRoleResponse} is the response of the role
     * @since 1.0
     * @author lyhou
     */
    UserRoleResponse getRole(String uuid);

    /**
     * Delete a role
     * @param uuid is the unique identifier of the role
     * @since 1.0
     * @author lyhou
     */
    void deleteRole(String uuid);

}
