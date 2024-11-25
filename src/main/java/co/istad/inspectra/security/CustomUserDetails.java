package co.istad.inspectra.security;

import co.istad.inspectra.domain.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor

public class CustomUserDetails implements UserDetails {

    private User user;
    private String userUuid;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

       return user.getRoles().stream()
               .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
               .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getIsAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getIsAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getIsCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return !user.getIsEnabled();
    }


    public String getUserUuid() {
        return user.getUuid();
    }
}
