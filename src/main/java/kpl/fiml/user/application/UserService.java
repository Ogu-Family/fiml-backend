package kpl.fiml.user.application;

import kpl.fiml.user.domain.User;
import kpl.fiml.user.domain.UserRepository;
import kpl.fiml.user.dto.UserCreateRequest;
import kpl.fiml.user.dto.UserCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserCreateResponse createUser(UserCreateRequest request) {
        User savedUser = userRepository.save(request.toEntity());

        return new UserCreateResponse(savedUser.getId());
    }
}
