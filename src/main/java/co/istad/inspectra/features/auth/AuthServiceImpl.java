package co.istad.inspectra.features.auth;

import co.istad.inspectra.domain.User;
import co.istad.inspectra.domain.role.EnumRole;
import co.istad.inspectra.domain.role.Role;
import co.istad.inspectra.features.auth.dto.*;
import co.istad.inspectra.features.role.RoleRepository;
import co.istad.inspectra.mapper.UserMapper;
import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UserRegisterDto;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.security.CustomUserDetails;
import co.istad.inspectra.security.TokenGenerator;
import co.istad.inspectra.utils.EmailUtil;
import co.istad.inspectra.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {

    private static final int OTP_EXPIRATION_MINUTES = 2;

    private final TokenGenerator tokenGenerator;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final DaoAuthenticationProvider daoAuthenticationProvider;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final OtpUtil otpUtil;

    private final EmailUtil emailUtil;
    public AuthResponse login(AuthRequest authRequest) {

        User user = userRepository.findUsersByEmail(authRequest.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.getIsActive()) {

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not verified");
        }

        Authentication authentication = daoAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password())
        );
        return tokenGenerator.generateTokens(authentication);
    }



    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest)
    {
       Authentication authentication = jwtAuthenticationProvider.authenticate(
               new BearerTokenAuthenticationToken(refreshTokenRequest.refreshToken())
       );
       return tokenGenerator.generateTokens(authentication);
    }



    @Override
    public ResponseUserDto createUser(UserRegisterDto userRegisterDto){

        String otp = otpUtil.generateOtp();

        try {
            emailUtil.sendOtpEmail(userRegisterDto.email(), otp);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to send otp please try again");
        }

        if(userRepository.existsByEmail(userRegisterDto.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User with email " + userRegisterDto.email() + " already existed");
        }

        if(userRepository.existsByName(userRegisterDto.userName())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"User with username " + userRegisterDto.userName() + " already existed");
        }

        User user = new User();
        user.setUuid(UUID.randomUUID().toString());
        user.setFirstName(userRegisterDto.firstName());
        user.setLastName(userRegisterDto.lastName());
        user.setName(userRegisterDto.userName());
        user.setEmail(userRegisterDto.email());
        user.setIsVerified(true);
        user.setIsDeleted(false);

        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsEnabled(false);


        user.setRegisteredDate(LocalDateTime.now());
//        set role for user
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findRoleByRoleName(EnumRole.ROLE_USER).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role is not found.")));
        user.setRoles(roles);
        // set password for users
        user.setIsActive(false);
        user.setPassword(passwordEncoder.encode(userRegisterDto.password()));
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());

        userRepository.save(user);

        return userMapper.mapFromUserToUserResponseDto(user);
    }

    @Override
    public String verifyAccount(VerifyAccountRequest verifyAccountRequest) {

        User user = userRepository.findUsersByEmail(verifyAccountRequest.email())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email: " + verifyAccountRequest.email()));

        if (user.getOtp().equals(verifyAccountRequest.otp()) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (2 * 60)) {

//            set user as verified
            user.setIsActive(true);

            userRepository.save(user);

            return "OTP verified ";
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP or OTP expired please try again");
    }

    @Override
    public String resendOtp(String email) {

        User user = userRepository.findUsersByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email: " + email));

        String otp = otpUtil.generateOtp();

        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to send otp please try again");
        }

        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());

        userRepository.save(user);

        return "Email sent... please verify account within 2 minutes";
    }

    @Override
    public String RequestforgotPassword(String email) {

        User user = findUserByEmail(email);

        String otp = otpUtil.generateOtp();

        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to send otp please try again");
        }

        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());

        userRepository.save(user);


        return "OTP sent to your email. Please verify and reset your password within 2 minutes.";
    }

    @Override
    public String forgotPassword(ForgetPassword forgetPassword) {
        User user = userRepository.findUsersByEmail(forgetPassword.email())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email: " + forgetPassword.email()));

        // Check if the OTP is valid and not expired
        if (isOtpValid(user, forgetPassword.otp())) {

            if (!forgetPassword.newPassword().equals(forgetPassword.confirmPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and confirm password do not match");
            }

            user.setPassword(passwordEncoder.encode(forgetPassword.newPassword()));

            user.setOtp(null);  // Clear OTP after a successful password reset

            user.setOtpGeneratedTime(null);

            userRepository.save(user);

            return "Password reset successful.";

        } else {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired OTP.");

        }
    }

    @Override
    public String changePassword(ChangePassword changePassword, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        if (customUserDetails == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User Unauthorized");
        }

        String email = customUserDetails.getUsername();

        User usr = findUserByEmail(email);

        if (usr == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with this email: " + email);
        }

        if(passwordEncoder.matches(changePassword.oldPassword(), usr.getPassword())){

            if (!changePassword.newPassword().equals(changePassword.confirmPassword()))
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password and confirm password do not match");
            }

            usr.setPassword(passwordEncoder.encode(changePassword.newPassword()));

            userRepository.save(usr);

            return "Password changed successfully";
        }

        else {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is incorrect");
        }
    }

    @Override
    public void initUserWithAuth(InitUserRequest initUserRequest) {

        //auto init user
        User user = new User();

        if (userRepository.existsByEmail(initUserRequest.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with email " + initUserRequest.email() + " already existed");
        }

        user.setEmail(initUserRequest.email());
        user.setFirstName("User");
        user.setLastName("User");
        user.setName(initUserRequest.email().split("@")[0]);
        user.setIsActive(true);
        user.setIsDeleted(false);
        user.setIsVerified(true);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);

        Role role = roleRepository.findRoleByRoleName(EnumRole.ROLE_USER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role is not found."));

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        user.setRoles(roles);
        user.setIsEnabled(false);
        user.setRegisteredDate(LocalDateTime.now());
        user.setUuid(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode("123456"));

        userRepository.save(user);



    }


    //make function find user by email
    public User findUserByEmail(String email){

        return userRepository.findUsersByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "User not found with this email: " + email));

    }

    // Check if OTP is valid
    private boolean isOtpValid(User user, String otp) {
        // Check OTP matches and is within expiration time
        return user.getOtp() != null && user.getOtp().equals(otp) &&
                Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).toMinutes() < OTP_EXPIRATION_MINUTES;

    }

}
