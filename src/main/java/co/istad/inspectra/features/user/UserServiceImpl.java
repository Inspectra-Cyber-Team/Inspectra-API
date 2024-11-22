package co.istad.inspectra.features.user;

import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UpdateUserDto;
import co.istad.inspectra.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;
    @Override
    public ResponseUserDto getUserById(String uuid) {

        User findUser = userRepository.findUserByUuid(uuid);

        if(findUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with uuid: "+uuid);
        }

        return userMapper.mapFromUserToUserResponseDto(findUser);
    }

    @Override
    public ResponseUserDto updateUser(String uuid, UpdateUserDto updateUserDto) {

        User findUser = userRepository.findUserByUuid(uuid);

        if(findUser == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found with uuid: "+uuid);
        }

        userMapper.updateUserFromRequest(findUser,updateUserDto);

        return userMapper.mapFromUserToUserResponseDto(userRepository.save(findUser));


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
    public String updateProfile(String uuid, String thumbnails) {

        User user = userRepository.findUserByUuid(uuid);

        user.setProfile(thumbnails);

        return "Update user profile Successfully";
    }
}
