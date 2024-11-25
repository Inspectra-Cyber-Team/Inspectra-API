package co.istad.inspectra.security;

import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.user.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class JwtToUserConverter implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    private final UserRepository userRepository;


    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {

        User user = userRepository.findUsersByEmail(source.getSubject()).orElseThrow(()-> new BadCredentialsException("Invalid Token"));

        CustomUserDetails userDetail = new CustomUserDetails();

        userDetail.setUser(user);

        // Log the authorities for debugging purposes
        userDetail.getAuthorities().forEach(authority -> {
            System.out.println("Authority: " + authority.getAuthority());
        });



            return new UsernamePasswordAuthenticationToken(userDetail,"",userDetail.getAuthorities());
    }
}
