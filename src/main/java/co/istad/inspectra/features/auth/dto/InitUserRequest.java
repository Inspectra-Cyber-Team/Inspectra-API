package co.istad.inspectra.features.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record InitUserRequest(

        @NotBlank(message = "Email is required")
        @Email(message = "Email is invalid format")
        String email

) {
}
