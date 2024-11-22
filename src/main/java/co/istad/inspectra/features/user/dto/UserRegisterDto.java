package co.istad.inspectra.features.user.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Builder
@Validated
public record UserRegisterDto(

        @NotBlank(message = "First Name is required")
        @Size(max = 50, message = "First Name should be between 1 to 50 characters")
        String firstName,

        @NotBlank(message = "Last Name is required")
        @Size(max = 50, message = "Last Name should be between 1 to 50 characters")
        String lastName,
        @NotBlank(message = "Username is required")
        @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "Username should be alphanumeric and between 3 to 20 characters")
        String userName,
        @Email(message = "Email format is not correct!")
        @NotBlank(message = "Email is required")
        String email,
        @NotBlank(message = "Password is required")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "Password should be at least 8 characters long and contain at least one uppercase letter, one lowercase letter and one number")
        String password,
        @NotBlank(message = "Confirm Password is required")
        String confirmPassword
)
{ }
