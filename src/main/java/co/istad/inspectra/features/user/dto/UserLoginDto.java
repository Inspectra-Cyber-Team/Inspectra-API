package co.istad.inspectra.features.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Builder
@Validated
public record UserLoginDto(
        @Email(message = "Email should be valid")
        String email,

        @NotNull(message = "Password should not be null")
        String password
) {
}
