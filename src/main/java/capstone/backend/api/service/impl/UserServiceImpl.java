package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.UserInforResponse;
import capstone.backend.api.entity.ApiResponse.UsersResponse;
import capstone.backend.api.entity.*;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.UserService;
import capstone.backend.api.utils.DateUtils;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private CommonProperties commonProperties;

    private JwtUtils jwtUtils;

    private DepartmentServiceImpl departmentService;

    private ExecuteServiceImpl executeService;

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
        Page<User> users = userRepository.findAllByDepartmentId(id, PageRequest.of(page, size));
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
    public ResponseEntity<?> getUserInformation(String jwtToken) throws Exception {

        String email = jwtUtils.getUserNameFromJwtToken(jwtToken.substring(5));
        User user = userRepository.findByEmail(email).get();
        Department department = departmentService.getDepartmentById(user.getDepartment().getId());
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        ArrayList<Execute> executes = executeService.getListExecuteByUserId(user.getId());

        UserInforResponse userInforResponse = UserInforResponse.builder().id(user.getId())
                .email(email).fullName(user.getFullName())
                .avatarUrl(user.getAvatarImage())
                .dob(user.getDob()).gender(user.getGender())
                .point(user.getPoint()).roles(roles)
                .department(new UserInforResponse().departmentResponse(department.getId(), department.getName()))
                .projects(new UserInforResponse().projectResponses(executes)).build();

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(userInforResponse).build()
        );
    }

}
