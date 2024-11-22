package co.istad.inspectra.mapper;

import co.istad.inspectra.domain.User;
import co.istad.inspectra.features.user.dto.ResponseUserDto;
import co.istad.inspectra.features.user.dto.UpdateUserDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ResponseUserDto mapFromUserToUserResponseDto(User user);
    List<ResponseUserDto> mapFromListOfUserToListOfUserDto(List<User> userList);



@Mapping(target = "id", ignore = true)
@Mapping(target = "uuid", ignore = true)
@Mapping(target = "email", ignore = true)
@Mapping(target = "password", ignore = true)
@Mapping(target = "registeredDate", ignore = true)
@Mapping(target = "updatedDate", ignore = true)
@Mapping(target = "isDeleted", ignore = true)
@Mapping(target = "isVerified", ignore = true)
@Mapping(target = "isActive", ignore = true)
@Mapping(target = "isAccountNonExpired", ignore = true)
@Mapping(target = "isAccountNonLocked", ignore = true)
@Mapping(target = "isCredentialsNonExpired", ignore = true)
@Mapping(target = "isEnabled", ignore = true)
@Mapping(target = "projectSet", ignore = true)
@Mapping(target = "roles", ignore = true)
@Mapping(target = "authorities", ignore = true)
@Mapping(target = "otp", ignore = true)
@Mapping(target = "otpGeneratedTime", ignore = true)
@BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
void updateUserFromRequest(@MappingTarget User user, UpdateUserDto userDto);

}
