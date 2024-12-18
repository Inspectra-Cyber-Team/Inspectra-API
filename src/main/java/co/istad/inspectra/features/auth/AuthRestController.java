package co.istad.inspectra.features.auth;


import co.istad.inspectra.features.auth.dto.*;
import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UserRegisterDto;
import co.istad.inspectra.base.BaseRestResponse;
import co.istad.inspectra.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController

@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthServiceImpl authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest)
    {
       return BaseRestResponse.<AuthResponse>
               builder()
               .status(HttpStatus.OK.value())
               .timestamp(LocalDateTime.now())
               .data(authService.login(authRequest))
               .message("Login successfully")
               .build();
    }


   @PostMapping("/register")
   @ResponseStatus(HttpStatus.CREATED)
    public BaseRestResponse<ResponseUserDto> signup(@Valid @RequestBody UserRegisterDto userRegisterDto)
    {
        return BaseRestResponse.<ResponseUserDto>builder()
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .data(authService.createUser(userRegisterDto))
                .message("User created successfully")
                .build();
    }


    @PostMapping("/refreshToken")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {

        return BaseRestResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(authService.refreshToken(request))
                .message("Token refreshed successfully")
                .build();

    }

    @PutMapping("/verify-account")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> verifyAccount(@Valid @RequestBody VerifyAccountRequest verifyAccountRequest) {

        return BaseRestResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(authService.verifyAccount(verifyAccountRequest))
                .message("Account verified successfully")
                .build();
    }


    @PutMapping("/resend-otp")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> resendOtp(@RequestParam String email) {

        return BaseRestResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(authService.resendOtp(email))
                .message("OTP sent successfully")
                .build();

    }

    @PutMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public BaseRestResponse<String> forgotPassword(@Valid @RequestBody ForgetPassword forgetPassword) {

        return BaseRestResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(authService.forgotPassword(forgetPassword))
                .message("Password reset successfully")
                .build();

    }

    @PutMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public BaseRestResponse<String> changePassword(@Valid @RequestBody ChangePassword changePassword, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        return BaseRestResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(authService.changePassword(changePassword, customUserDetails))
                .message("Password changed successfully")
                .build();

    }

    @PostMapping("/init-user")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseRestResponse<ResponseUserDto> initUser(@Valid @RequestBody InitUserRequest initUserRequest) {

        authService.initUserWithAuth(initUserRequest);

        return BaseRestResponse.<ResponseUserDto>builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CREATED.value())
                .timestamp(LocalDateTime.now())
                .message("User has been initialized successfully.")
                .build();

    }




}
