package co.istad.inspectra.security;

import co.istad.inspectra.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        var user = userRepository.findUsersByEmail(email).orElseThrow();

        CustomUserDetails customUserDetails = new CustomUserDetails();

        customUserDetails.setUser(user);

        return customUserDetails;
    }
}
