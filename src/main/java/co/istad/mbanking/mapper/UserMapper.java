package co.istad.mbanking.mapper;

import co.istad.mbanking.domain.User;
import co.istad.mbanking.features.auth.dto.RegisterRequest;
import co.istad.mbanking.features.user.dto.CreateUserRequest;
import co.istad.mbanking.features.user.dto.UserResponse;
import co.istad.mbanking.features.user.dto.UserUpdateRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromCreateUserRequest(CreateUserRequest createUserRequest);

    User fromRegisterRequest(RegisterRequest registerRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUserUpdateRequest(UserUpdateRequest userUpdateRequest, @MappingTarget User user);

    UserResponse toUserResponse(User user);
}
