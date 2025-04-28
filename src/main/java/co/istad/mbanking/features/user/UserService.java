package co.istad.mbanking.features.user;

import co.istad.mbanking.features.user.dto.CreateUserRequest;
import co.istad.mbanking.features.user.dto.UserResponse;
import co.istad.mbanking.features.user.dto.UserUpdateRequest;
import org.springframework.data.domain.Page;

public interface UserService {

    void register(CreateUserRequest createUserRequest);

    UserResponse updateByUuid(String uuid, UserUpdateRequest userUpdateRequest);

    Page<UserResponse> findList(int page, int limit);

    UserResponse findByUuid(String uuid);

    void blockByUuid(String uuid);

    void deleteByUuid(String uuid);

    String updateProfileImage(String uuid, String mediaName);


}
