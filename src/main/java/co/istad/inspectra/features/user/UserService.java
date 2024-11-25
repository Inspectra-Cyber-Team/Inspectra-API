package co.istad.inspectra.features.user;


import co.istad.inspectra.base.BaseSpecification;
import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UpdateUserDto;
import co.istad.inspectra.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

/**
 *   UserService interface
 *   @version 1.0
 * @author : lyhou
 *
 */

public interface UserService {

    /**
     * Get user by id
     * @return {@link ResponseUserDto}
     * @param uuid is identifier of user
     * @author : lyhou
     */
    ResponseUserDto getUserByUuid(String uuid);


    /**
     * Delete user
     * @param uuid is identifier of user
     * @author : lyhou
     */
    void deleteUser(String uuid);

    /**
     * Get all users
     * @return {@link List<ResponseUserDto>}
     * @author : lyhou
     *
     */
    List<ResponseUserDto> getAllUsers();


    /**
     * Get all user by page
     * @return {@link ResponseUserDto}
     * @author : lyhou
     */

    Page<ResponseUserDto> getAllUsersByPage(int page, int size);

    /**
     * Update user profile
     *
     *
     */

    ResponseUserDto updateProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails, UpdateUserDto updateUserDto);


    /**
     * Block user
     * @param uuid is identifier of user
     * @return {@link ResponseUserDto}
     * @author : lyhou
     */
    void blockUser(String uuid);

    /**
     * Unblock user
     * @param uuid is identifier of user
     * @return {@link ResponseUserDto}
     * @author : lyhou
     */

    void unblockUser(String uuid);

    List<ResponseUserDto> getUserByFilter(BaseSpecification.FilterDto filterDto);





}
