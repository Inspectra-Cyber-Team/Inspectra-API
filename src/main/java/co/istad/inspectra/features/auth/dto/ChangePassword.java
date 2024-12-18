package co.istad.inspectra.features.auth.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ChangePassword(

        @NotBlank(message = "Old password should not be empty")
        String oldPassword,

        @NotBlank(message = "New password should not be empty")
        String newPassword,

        @NotBlank(message = "Confirm password should not be empty")
        String confirmPassword
){ }
