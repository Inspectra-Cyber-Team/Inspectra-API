package co.istad.inspectra.features.user;


import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UpdateUserDto;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 *   UserService interface
 *   @version 1.0
 * @Author : lyhou
 *
 */

public interface UserService {

    /**
     * Get user by id
     * @return {@link ResponseUserDto}
     * @param uuid is identifier of user
     * @author : lyhou
     */
    ResponseUserDto getUserById(String uuid);

    /**
     * Update user
     * @return @link ResponseUserDto}
     * @param uuid, updateUserDto
     * author: lyhou
     */
    ResponseUserDto updateUser(String uuid,UpdateUserDto updateUserDto);

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
     * @Author : lyhou
     */

    Page<ResponseUserDto> getAllUsersByPage(int page, int size);

    /**
     * Update user profile
     *
     *
     */

    String updateProfile(String uuid, String thumbnails);




}
