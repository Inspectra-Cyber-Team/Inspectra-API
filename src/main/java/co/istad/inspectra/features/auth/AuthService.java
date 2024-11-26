package co.istad.inspectra.features.auth;


import co.istad.inspectra.features.auth.dto.*;
import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UserRegisterDto;

/**
 * AuthService
 * @author : @lyhou
 * @since : 1.0 (2024)
 */
public interface AuthService {
    /**
     * Login user
     * @param authRequest is the request object
     * @return {@link AuthResponse}
     * author : @lyhou
     *
     */
    AuthResponse login(AuthRequest authRequest);

    /**
     * Create user
     * @param userRegisterDto is the request object
     * @return {@link ResponseUserDto}
     * author : @lyhou
     *
     */
    ResponseUserDto createUser(UserRegisterDto userRegisterDto);

    /**
     * Refresh token
     * @param refreshToken is the request object
     * @return {@link AuthResponse}
     * author : @lyhou
     *
     */
    AuthResponse refreshToken(RefreshTokenRequest refreshToken);

    /**
     * Verify account
     * @param verifyAccountRequest is the request object
     * @return {@link String}
     * author : @lyhou
     *
     */

    String verifyAccount(VerifyAccountRequest verifyAccountRequest);

    /**
     * Resend OTP
     * @param email is the request object
     * @return {@link String}
     * author : @lyhou
     *
     */

    String resendOtp(String email);

    /**
     * Forgot password
     * @param email is the request object
     * @return {@link String}
     * author : @lyhou
     *
     */
     String forgotPassword(String email);

    /**
     * Reset password
     * @param forgetPassword is the request object
     *
     */
    String forgotPassword(ForgetPassword forgetPassword);

    /**
     * Reset password
     * @param changePassword is the request object
     * @author : @lyhou
     */
    String changePassword(ChangePassword changePassword);




}
