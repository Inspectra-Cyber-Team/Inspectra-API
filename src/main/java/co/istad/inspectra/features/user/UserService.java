package co.istad.inspectra.features.user;


import co.istad.inspectra.base.BaseSpecification;
import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UpdateUserDto;
import co.istad.inspectra.features.user.dto.UserDetailsResponse;
import co.istad.inspectra.features.user.dto.UserRegisterDto;
import co.istad.inspectra.security.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


import java.util.List;

/**
 *   UserService interface
 *   @version 1.0
 * @author : lyhou
 * @see UserServiceImpl
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
     * @param customUserDetails is custom user details
     * @return {@link ResponseUserDto}
     *
     */

    ResponseUserDto updateProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails, UpdateUserDto updateUserDto);


    /**
     * Block user
     * @param uuid is identifier of user
     * @author : lyhou
     */
    void blockUser(String uuid);

    /**
     * Unblock user
     * @param uuid is identifier of user
     * @author : lyhou
     */

    void unblockUser(String uuid);


    /**
     * Get user by filter
     * @param filterDto is filter dto
     * @return {@link List<ResponseUserDto>}
     */
    List<ResponseUserDto> getUserByFilter(BaseSpecification.FilterDto filterDto);

    /**
     * Create admin user
     * @param userRegisterDto is user register dto
     * @return {@link ResponseUserDto}
     */
    ResponseUserDto createAdmin(UserRegisterDto userRegisterDto);


    UserDetailsResponse getUserDetails(String uuid);

    /**
     * Get all admin user
     * @return {@link Page<ResponseUserDto>}
     * @param page is page number
     *             @param size is size of page
     */

    Page<ResponseUserDto> getAllAdminUsers(int page, int size);


    /**
     * count all user
     */
    int countAllUser();


}

