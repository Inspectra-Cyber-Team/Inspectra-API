package co.istad.inspectra.features.user;


import co.istad.inspectra.base.BaseSpecification;
import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UpdateUserDto;
import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.features.user.dto.UserRegisterDto;
import co.istad.inspectra.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserServiceImpl userService;


    @GetMapping("/find")
    public BaseRestResponse<ResponseUserDto> findUserByUuid(@RequestParam String userUuid) {

        return BaseRestResponse
                .<ResponseUserDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(userService.getUserByUuid(userUuid))
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
    public BaseRestResponse<List<ResponseUserDto>> getAllUsers() {

        return BaseRestResponse
                .<List<ResponseUserDto>>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(userService.getAllUsers())
                .message("Users found successfully.")
                .build();
    }


    @DeleteMapping("/{userUuid}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    public BaseRestResponse<ResponseUserDto> deleteUserByUuid(@PathVariable String userUuid) {

        userService.deleteUser(userUuid);

        return BaseRestResponse
                .<ResponseUserDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message("User has been deleted successfully.")
                .build();

    }

    @PutMapping("/profile")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<ResponseUserDto> updateProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody UpdateUserDto updateUserDto) {

        return BaseRestResponse
                .<ResponseUserDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(userService.updateProfile(customUserDetails,updateUserDto))
                .message("Profile has been updated successfully.")
                .build();
    }


    @PutMapping("/block/{userUuid}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BaseRestResponse<ResponseUserDto> blockUser(@PathVariable String userUuid) {

        userService.blockUser(userUuid);

        return BaseRestResponse
                .<ResponseUserDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message("User has been blocked successfully.")
                .build();

    }

    @PutMapping("/unblock/{userUuid}")
    @PreAuthorize("hasRole('ADMIN')")
    public BaseRestResponse<ResponseUserDto> unblockUser(@PathVariable String userUuid) {

        userService.unblockUser(userUuid);

        return BaseRestResponse
                .<ResponseUserDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .message("User has been unblocked successfully.")
                .build();

    }


    @PostMapping("/filter")
    public BaseRestResponse<List<ResponseUserDto>> getUsersByFilter(@RequestBody BaseSpecification.FilterDto filterDto) {

        return BaseRestResponse.<List<ResponseUserDto>>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(userService.getUserByFilter(filterDto))
                .message("Users found successfully.")
                .build();

    }

    @Operation(summary = "create admin")
    @PostMapping("/admin")
    public BaseRestResponse<ResponseUserDto> createAdmin(@Valid @RequestBody UserRegisterDto userRegisterDto) {

        return BaseRestResponse
                .<ResponseUserDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.OK.value())
                .data(userService.createAdmin(userRegisterDto))
                .message("Admin has been created successfully.")
                .build();
    }









}
