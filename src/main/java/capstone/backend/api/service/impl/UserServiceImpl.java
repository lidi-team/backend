package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.UsersResponse;
import capstone.backend.api.entity.User;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private CommonProperties commonProperties;

    @Override
    public ResponseEntity<?> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user == null ?
                ResponseEntity.badRequest().body(
                        ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                                .message(commonProperties.getMESSAGE_NOT_FOUND())
                                .build()
                )
                : ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(user)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getAllUsers(int size, int page) {
        if (size == 0) {
            size = 20;
        }

        Page<User> users = userRepository.findAll(PageRequest.of(page, size));
        int totalPage = users.getTotalPages();

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(UsersResponse.builder()
                                .page(page).size(size)
                                .totalPage(totalPage)
                                .users(users).build()
                        ).build()
        );
    }

    @Override
    public ResponseEntity<?> getAllUsersByDepartmentId(long id, int page, int size) {
        Page<User> users = userRepository.findAllByDepartmentId(id,PageRequest.of(page, size));
        int totalPage = users.getTotalPages();

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(UsersResponse.builder()
                                .page(page).size(size)
                                .totalPage(totalPage)
                                .users(users).build()
                        ).build()
        );
    }
}
