package co.istad.inspectra.features.user;


import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UpdateUserDto;
import co.istad.inspectra.base.BaseRestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserServiceImpl userService;


    @GetMapping("/find")
    public BaseRestResponse<Object> findUserByUuid(@RequestParam String userUuid) {

        return BaseRestResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(userService.getUserById(userUuid))
                .message("User has been found successfully.")
                .build();
    }


    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public Page<ResponseUserDto> getAllUsersByPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size) {

            return userService.getAllUsersByPage(page, size);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
    public BaseRestResponse<Object> getAllUsers() {

        return BaseRestResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(userService.getAllUsers())
                .message("Users found successfully.")
                .build();
    }



    @PutMapping("/{userUuid}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public BaseRestResponse<Object> updateUserByUuid(@PathVariable String userUuid, @RequestBody UpdateUserDto updateUserDto) {

        return BaseRestResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(userService.updateUser(userUuid, updateUserDto))
                .message("Users has been updated successfully.")
                .build();
    }

    @DeleteMapping("/{userUuid}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public BaseRestResponse<Object> deleteUserByUuid(@PathVariable String userUuid) {

        userService.deleteUser(userUuid);

        return BaseRestResponse
                .builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message("User has been deleted successfully.")
                .build();

    }


    @PutMapping("/profile/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public BaseRestResponse<String> updateUserProfile(@PathVariable String uuid, String thumbnails)
    {
        return BaseRestResponse.<String>builder()
                .timestamp(LocalDateTime.now())
                .data(userService.updateProfile(uuid,thumbnails))
                .message("Update Profile Successfully")
                .build();
    }
}
