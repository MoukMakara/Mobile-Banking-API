package co.istad.mbanking.features.user;

import co.istad.mbanking.features.user.dto.CreateUserRequest;
import co.istad.mbanking.features.user.dto.UserResponse;
import co.istad.mbanking.features.user.dto.UserUpdateRequest;
import org.springframework.data.domain.Page;

public interface UserService {

    UserResponse register(CreateUserRequest createUserRequest);

    UserResponse updateByUuid(String uuid, UserUpdateRequest userUpdateRequest);

    Page<UserResponse> findList(int page, int limit);

    UserResponse findByUuid(String uuid);

    // update role
//    UserResponse updateRoleByUuid(String uuid, String roleName);

    void setBlockAndUnBlockByUuid(String uuid, boolean status);

    void deleteByUuid(String uuid);

    String updateProfileImage(String uuid, String mediaName);
}
