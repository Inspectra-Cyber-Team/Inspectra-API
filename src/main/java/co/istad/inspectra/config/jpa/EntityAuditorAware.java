package co.istad.inspectra.config.jpa;

import co.istad.inspectra.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EntityAuditorAware implements AuditorAware<String> {



    @NotNull
    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails user)) {
            return Optional.of("cyber");
        }

        return Optional.of(user.getUsername());
    }
}
