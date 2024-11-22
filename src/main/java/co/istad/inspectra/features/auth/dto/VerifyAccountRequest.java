package co.istad.inspectra.features.auth.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record VerifyAccountRequest (
        @NotBlank(message = "Email is required")
        @Email(message = "Email format is not correct!")
        String email,

        @NotBlank(message = "OTP is required")
        @Positive(message = "OTP must be a positive number")
        @Pattern(regexp = "^[0-9]{6}$", message = "OTP must be a 6 digit number")
        String otp
){
}
