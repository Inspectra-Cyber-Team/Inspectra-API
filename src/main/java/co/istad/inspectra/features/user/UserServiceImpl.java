package co.istad.inspectra.features.user;

import co.istad.inspectra.base.BaseSpecification;
import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UpdateUserDto;
import co.istad.inspectra.mapper.UserMapper;
import co.istad.inspectra.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final BaseSpecification<User> baseSpecification;
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

        findUser.setIsActive(false);

        userRepository.save(findUser);

    }

    @Override
    public void unblockUser(String uuid) {

            User findUser = userRepository.findUserByUuid(uuid);

            if(findUser == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with uuid: "+uuid);
            }

            findUser.setIsActive(true);

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


    // Find all users matching the specification



}
