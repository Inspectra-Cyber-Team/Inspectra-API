package co.istad.inspectra.init;

import co.istad.inspectra.domain.Authority;
import co.istad.inspectra.domain.Topic;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.domain.role.EnumRole;
import co.istad.inspectra.domain.role.Role;
import co.istad.inspectra.features.authority.AuthorityRepository;
import co.istad.inspectra.features.role.RoleRepository;
import co.istad.inspectra.features.user.UserRepository;
import co.istad.inspectra.features.topic.TopicRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final AuthorityRepository authorityRepository;

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TopicRepository topicRepository;

    @PostConstruct
    void init()
    {
        initAuthority();
        initRole();
        initUser();
        initTopic();
    }


    void initAuthority() {
        // auth generate role (USER,ADMIN)
        if (authorityRepository.count() < 5) {
            List<String> authorityNames = List.of(
                   "user:read",
                    "user:write",
                    "user:update",
                    "user:delete",
                    "admin:control"

            );

            List<Authority> authorities = authorityNames.stream()
                    .map(this::createAuthority)
                    .toList();

            authorityRepository.saveAll(authorities);
        }
    }


    private Authority createAuthority(String authorityName) {

        Authority authority = new Authority();

        authority.setAuthorityName(authorityName);

        authority.setUuid(UUID.randomUUID().toString());

        return authorityRepository.save(authority);
    }

    void initRole() {
        // auto generate role (USER,ADMIN)
        if (roleRepository.count() < 2) {
            List<String> roleNames = List.of(
                    "USER",
                    "ADMIN"
            );

            List<Role> roles = roleNames.stream()
                    .map(this::createRole)
                    .toList();

            roleRepository.saveAll(roles);
        }
    }

    private Role createRole(String roleName) {

        Role role = new Role();

        role.setRoleName(EnumRole.valueOf("ROLE_" + roleName));

        role.setUuid(UUID.randomUUID().toString());

        return roleRepository.save(role);
    }


    void initUser() {
        if (userRepository.count() < 2) {
            User user = new User();

            user.setEmail("lyhou282@gmail.com");
            user.setPassword(passwordEncoder.encode("lyhou123"));
            user.setFirstName("Phiv");
            user.setLastName("Lyhou");
            user.setName("lyhou");
            user.setUuid(UUID.randomUUID().toString());
            user.setBio("I am a software engineer");

            // Fetching and assigning the Role
            Role role = roleRepository.findRoleByRoleName(EnumRole.ROLE_USER)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role is not found."));

            user.setRoles(Set.of(role));

            // Fetching and assigning the Authority
            Set<Authority> authorities = new HashSet<>(authorityRepository.findAll());
            user.setAuthorities(authorities);

            user.setIsActive(true);
            user.setIsDeleted(false);
            user.setIsVerified(true);
            user.setIsEnabled(false);
            user.setIsAccountNonExpired(true);
            user.setIsAccountNonLocked(true);
            user.setIsCredentialsNonExpired(true);
            user.setOtp("123456");
            user.setRegisteredDate(LocalDateTime.now());
            user.setOtpGeneratedTime(null);
            user.setRegisteredDate(null);

            userRepository.save(user);


            //init user admin
            User user1 = new User();

            user1.setEmail("lyhou@gmail.com");
            user1.setPassword(passwordEncoder.encode("lyhou123"));
            user1.setFirstName("Phiv");
            user1.setLastName("Lyhou");
            user1.setName("lyhou");
            user1.setUuid(UUID.randomUUID().toString());
            user1.setBio("I am a software engineer");

            // Fetching and assigning the Role
            Role role1 = roleRepository.findRoleByRoleName(EnumRole.ROLE_ADMIN)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role is not found."));

            user1.setRoles(Set.of(role1));

            // Fetching and assigning the Authority
            Set<Authority> authorities1 = new HashSet<>(authorityRepository.findAll());
            user1.setAuthorities(authorities1);

            user1.setIsActive(true);
            user1.setIsDeleted(false);
            user1.setIsVerified(true);
            user1.setIsEnabled(false);
            user1.setIsAccountNonExpired(true);
            user1.setIsAccountNonLocked(true);
            user1.setIsCredentialsNonExpired(true);
            user1.setOtp("123456");
            user1.setOtpGeneratedTime(null);
            user1.setRegisteredDate(null);

            userRepository.save(user1);


        }

    }

        void initTopic() {

            if (topicRepository.count() < 5) {
                Topic topic = new Topic();
                topic.setName("XSS Injection");
                topic.setUuid(UUID.randomUUID().toString());
                topicRepository.save(topic);

                Topic topic1 = new Topic();
                topic1.setName("Programming");
                topic1.setUuid(UUID.randomUUID().toString());
                topicRepository.save(topic1);

                Topic topic2 = new Topic();
                topic2.setName("White Hat Hacking");
                topic2.setUuid(UUID.randomUUID().toString());
                topicRepository.save(topic2);

                Topic topic3 = new Topic();
                topic3.setName("Selt Improvement");
                topic3.setUuid(UUID.randomUUID().toString());
                topicRepository.save(topic3);

                Topic topic4 = new Topic();
                topic4.setName("CIA Triad");
                topic4.setUuid(UUID.randomUUID().toString());
                topicRepository.save(topic4);

                Topic topic5 = new Topic();
                topic5.setName("Chatgpt");
                topic5.setUuid(UUID.randomUUID().toString());
                topicRepository.save(topic5);


                Topic topic6 = new Topic();
                topic6.setName("Linux Command");
                topic6.setUuid(UUID.randomUUID().toString());
                topicRepository.save(topic6);



            }
    }

}
