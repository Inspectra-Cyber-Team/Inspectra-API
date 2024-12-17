package co.istad.inspectra.features.user;

import co.istad.inspectra.base.BaseSpecification;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.domain.role.EnumRole;
import co.istad.inspectra.domain.role.Role;
import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UpdateUserDto;
import co.istad.inspectra.features.user.dto.UserDetailsResponse;
import co.istad.inspectra.features.user.dto.UserRegisterDto;
import co.istad.inspectra.features.role.RoleRepository;
import co.istad.inspectra.mapper.UserMapper;
import co.istad.inspectra.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final BaseSpecification<User> baseSpecification;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    @Override
    public ResponseUserDto getUserByUuid(String uuid) {

        User findUser = userRepository.findUserByUuid(uuid);

        if(findUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with uuid: "+uuid);
        }

        return userMapper.mapFromUserToUserResponseDto(findUser);
    }


    @Override
    public void deleteUser(String uuid) {

        User findUser = userRepository.findUserByUuid(uuid);

        if(findUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with uuid: "+uuid);
        }

        findUser.setIsDeleted(true);

        userRepository.save(findUser);

    }

    @Override
    public List<ResponseUserDto> getAllUsers() {

        List<User> userList = userRepository.findAll();

        if(userList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User is empty");
        }

        return userMapper.mapFromListOfUserToListOfUserDto(userList);
    }

    @Override
    public Page<ResponseUserDto> getAllUsersByPage(int page, int size) {

        if(page < 0 || size < 0){

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Page and size must be greater than 0");

        }

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,"id"));

        Page<User> userPage = userRepository.findAll(pageRequest);

        List<User> userFilter = userRepository.findAll().stream()
                .filter(user -> !user.getIsDeleted())
                .toList();

        if(userPage.isEmpty()){

            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User is empty");

        }

        return new PageImpl<>
                (userMapper.mapFromListOfUserToListOfUserDto(userFilter),
                        pageRequest,userFilter.size());

    }


    @Override
    public ResponseUserDto updateProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails, UpdateUserDto updateUserDto) {

        if(customUserDetails == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"User unauthorized");

        }

        String uuid = customUserDetails.getUserUuid();

        User user = userRepository.findUserByUuid(uuid);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with uuid: "+uuid);
        }

        userMapper.updateUserFromRequest(user,updateUserDto);

        userRepository.save(user);

        return userMapper.mapFromUserToUserResponseDto(user);

    }

    @Override
    public void blockUser(String uuid) {

        User findUser = userRepository.findUserByUuid(uuid);

        if(findUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with uuid: "+uuid);
        }

        findUser.setIsEnabled(true);

        userRepository.save(findUser);

    }

    @Override
    public void unblockUser(String uuid) {

            User findUser = userRepository.findUserByUuid(uuid);

            if(findUser == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with uuid: "+uuid);
            }

            findUser.setIsEnabled(false);

            userRepository.save(findUser);

    }

    @Override
    public List<ResponseUserDto> getUserByFilter(BaseSpecification.FilterDto filterDto) {

        List<BaseSpecification.SpecsDto> specsDtos = filterDto.getSpecsDto();

        Specification<User> specification = null;

        // Combine all filters using AND by default
        for (BaseSpecification.SpecsDto specsDto : specsDtos) {
            Specification<User> spec = baseSpecification.filter(specsDto);

            if (specification == null) {
                specification = spec; // Initialize the specification with the first condition
            } else {
                specification = specification.and(spec); // Combine conditions with AND
            }
        }

        // Find all users matching the combined specification
        List<User> users = userRepository.findAll(specification);

        // Map users to ResponseUserDto
        return users.stream()
                .map(userMapper::mapFromUserToUserResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public ResponseUserDto createAdmin(UserRegisterDto userRegisterDto) {


        if (userRepository.existsByEmail(userRegisterDto.email()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with email " + userRegisterDto.email() + " already existed");
        }

        if (userRepository.existsByName(userRegisterDto.userName()))
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with username " + userRegisterDto.userName() + " already existed");
        }

        User user = new User();

        user.setUuid(UUID.randomUUID().toString());
        user.setFirstName(userRegisterDto.firstName());
        user.setLastName(userRegisterDto.lastName());
        user.setName(userRegisterDto.userName());
        user.setEmail(userRegisterDto.email());
        user.setIsDeleted(true);
        user.setIsVerified(false);
        user.setIsDeleted(false);

        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsEnabled(false);



        user.setRegisteredDate(LocalDateTime.now());
//        set role for user
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findRoleByRoleName(EnumRole.ROLE_ADMIN).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role is not found.")));
        user.setRoles(roles);
        // set password for users
        user.setIsActive(true);
        user.setPassword(passwordEncoder.encode(userRegisterDto.password()));
        user.setOtp("");
        user.setOtpGeneratedTime(LocalDateTime.now());

        userRepository.save(user);


        return userMapper.mapFromUserToUserResponseDto(user);
    }

    @Override
    public UserDetailsResponse getUserDetails(String uuid) {

        User findUser = userRepository.findUserByUuid(uuid);

        if(findUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with uuid: "+uuid);
        }

        return userMapper.mapFromUserToUserDetailsResponse(findUser);
    }

    @Override
    public Page<ResponseUserDto> getAllAdminUsers(int page, int size) {

        if (page < 0 || size < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page and size must be greater than 0");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.findAllAdmin(pageRequest);

        return userPage.map(userMapper::mapFromUserToUserResponseDto);
    }

    @Override
    public int countAllUser() {

        if (userRepository.findAll().isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User is empty");

        }

        return userRepository.findAll().size();
    }


}
