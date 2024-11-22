package co.istad.inspectra.features.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record ForgetPassword(

        @Email(message = "Email should be valid")
        @NotBlank(message = "Email should not be empty")
        String email,

        @Positive(message = "OTP should be positive")
        @NotBlank(message = "OTP should not be empty")
        String otp,

        @NotBlank(message = "New password should not be empty")
        String newPassword,

        @NotBlank(message = "Confirm password should not be empty")
        String confirmPassword

) {
}
