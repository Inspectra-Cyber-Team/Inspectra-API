package co.istad.inspectra.features.role;

import co.istad.inspectra.features.role.dto.RoleRequest;
import co.istad.inspectra.features.role.dto.RoleResponse;

import java.util.List;

/**
 * Service for managing user roles
 * @author : lyhou
 * @since : 1.0 (2024)
 */

public interface RoleService {

    /**
     * Get all user roles
     * @author lyhou
     * @since 1.0
     * @return {@link RoleResponse} is the response of all user roles
     */
     List <RoleResponse> getAllUserRoles();



    /**
     * Create a new role
     * @param roleRequest is used to create role in request body
     * @return {@link RoleResponse} is the response of the created role
     * @since 1.0
     * @author lyhou
     */
    RoleResponse createRole(RoleRequest roleRequest);


    /**
     * Update a role
     * @param uuid is the unique identifier of the role
     * @param roleRequest is the request body to update the role
     * @return {@link RoleResponse} is the response of the updated role
     * @since 1.0
     * @author lyhou
     */

    RoleResponse updateRole(String uuid, RoleRequest roleRequest);

    /**
     * Get a role
     * @param uuid is the unique identifier of the role
     * @return {@link RoleResponse} is the response of the role
     * @since 1.0
     * @author lyhou
     */
    RoleResponse getRole(String uuid);

    /**
     * Delete a role
     * @param uuid is the unique identifier of the role
     * @since 1.0
     * @author lyhou
     */
    void deleteRole(String uuid);

}
