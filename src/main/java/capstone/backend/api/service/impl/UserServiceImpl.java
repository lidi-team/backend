package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.UserChangePasswordDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.User.UserInforResponse;
import capstone.backend.api.entity.Department;
import capstone.backend.api.entity.Execute;
import capstone.backend.api.entity.Role;
import capstone.backend.api.entity.User;
import capstone.backend.api.repository.RoleRepository;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.UserService;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private CommonProperties commonProperties;

    private RoleRepository roleRepository;

    private JwtUtils jwtUtils;

    private DepartmentServiceImpl departmentService;

    private ExecuteServiceImpl executeService;

    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> getUserInformation(String jwtToken) throws Exception {

        String email = jwtUtils.getUserNameFromJwtToken(jwtToken.substring(5));
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        Department department = departmentService.getDepartmentById(user.getDepartment() == null ? 0 : user.getDepartment().getId());
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        ArrayList<Execute> executes = executeService.getListExecuteByUserId(user.getId());

        UserInforResponse userInforResponse = UserInforResponse.builder().id(user.getId())
                .email(email).fullName(user.getFullName())
                .avatarUrl(user.getAvatarImage())
                .dob(user.getDob()).gender(user.getGender())
                .star(user.getStar()).roles(roles)
                .department(department == null ? null
                        : new UserInforResponse().departmentResponse(department.getId(), department.getName()))
                .projects(new UserInforResponse().projectResponses(executes)).build();

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(userInforResponse).build()
        );
    }

    @Override
    public ResponseEntity<?> changePassword(UserChangePasswordDto userPassDto, String jwtToken) throws Exception {
        if (!validateChangePasswordInformation(userPassDto)) {
            logger.error("Parameter invalid!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }

        String email = jwtUtils.getUserNameFromJwtToken(jwtToken.substring(5));
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        if (!passwordEncoder.matches(userPassDto.getOldPassword(), user.getPassword())) {
            logger.error("Old password is incorrect!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_INVALID())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_INVALID()).build()
            );
        }

        String newPassword = passwordEncoder.encode(userPassDto.getNewPassword());
        user.setPassword(newPassword);
        userRepository.save(user);
        logger.info("update successful!");

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS()).build()
        );
    }

    @Override
    public ResponseEntity<?> saveAvatarLink(String url, String token) throws Exception {
        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }
        user.setAvatarImage(url);

        userRepository.save(user);

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS()).build()
        );
    }

    private boolean validateChangePasswordInformation(UserChangePasswordDto user) {
        return !user.getOldPassword().trim().isEmpty() &&
                !user.getNewPassword().trim().isEmpty();
    }

    public ResponseEntity<?> getAllUsers(String jwtToken) throws Exception {
        List<User> users = userRepository.findAll();
        List<UserInforResponse> listUserInforResponse = new ArrayList<>();

        users.forEach(user -> {
            try {
                Department department = departmentService.getDepartmentById(user.getDepartment() == null ? 0 : user.getDepartment().getId());
                Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
                ArrayList<Execute> executes = executeService.getListExecuteByUserId(user.getId());

                UserInforResponse userInforResponse = UserInforResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .avatarUrl(user.getAvatarImage())
                        .dob(user.getDob()).gender(user.getGender())
                        .star(user.getStar()).roles(roles)
                        .department(department == null ? null
                                : new UserInforResponse().departmentResponse(department.getId(), department.getName()))
                        .projects(new UserInforResponse().projectResponses(executes)).build();

                listUserInforResponse.add(userInforResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(listUserInforResponse).build()
        );
    }

    @Override
    public ResponseEntity<?> getUserInformationById(long id, String jwtToken) throws Exception {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message("Ngu").build()
            );
        }

        Department department = departmentService.getDepartmentById(user.getDepartment() == null ? 0 : user.getDepartment().getId());
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        ArrayList<Execute> executes = executeService.getListExecuteByUserId(user.getId());

        UserInforResponse userInforResponse = UserInforResponse.builder().id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .avatarUrl(user.getAvatarImage())
                .dob(user.getDob()).gender(user.getGender())
                .star(user.getStar()).roles(roles)
                .department(department == null ? null
                        : new UserInforResponse().departmentResponse(department.getId(), department.getName()))
                .projects(new UserInforResponse().projectResponses(executes)).build();

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(userInforResponse).build()
        );
    }

    @Override
    public ResponseEntity<?> getNumberStaff(String jwtToken) throws Exception {
        List<User> users = userRepository.findAll();
        List<UserInforResponse> listUserInforResponse = new ArrayList<>();

        users.forEach(user -> {
            try {
                Department department = departmentService.getDepartmentById(user.getDepartment() == null ? 0 : user.getDepartment().getId());
                Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
                ArrayList<Execute> executes = executeService.getListExecuteByUserId(user.getId());
                if (roles.contains("ROLE_USER") || roles.contains("ROLE_PM")) {

                    UserInforResponse userInforResponse = UserInforResponse.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .fullName(user.getFullName())
                            .avatarUrl(user.getAvatarImage())
                            .dob(user.getDob()).gender(user.getGender())
                            .star(user.getStar()).roles(roles)
                            .department(department == null ? null
                                    : new UserInforResponse().departmentResponse(department.getId(), department.getName()))
                            .projects(new UserInforResponse().projectResponses(executes)).build();

                    listUserInforResponse.add(userInforResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(listUserInforResponse.size()).build()
        );
    }

    @Override
    public ResponseEntity<?> getAllUsers(int page, int size, String sort, String jwtToken) throws Exception {
        List<User> users = userRepository.findAll(PageRequest.of(page, size, Sort.by(sort).ascending())).toList();
        List<UserInforResponse> listUserInforResponse = new ArrayList<>();

        users.forEach(user -> {
            try {
                Department department = departmentService.getDepartmentById(user.getDepartment() == null ? 0 : user.getDepartment().getId());
                Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
                ArrayList<Execute> executes = executeService.getListExecuteByUserId(user.getId());

                UserInforResponse userInforResponse = UserInforResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .avatarUrl(user.getAvatarImage())
                        .dob(user.getDob()).gender(user.getGender())
                        .star(user.getStar()).roles(roles)
                        .department(department == null ? null
                                : new UserInforResponse().departmentResponse(department.getId(), department.getName()))
                        .projects(new UserInforResponse().projectResponses(executes)).build();

                listUserInforResponse.add(userInforResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(listUserInforResponse).build()
        );
    }

    @Override
    public ResponseEntity<?> getStaffPaging(int page, int size, String sort, String jwtToken) throws Exception {
        Optional<Role> roleSatff = roleRepository.findById(Long.valueOf(5));

        List<User> users = userRepository.findAllByRolesContains(roleSatff.get(), PageRequest.of(page, size, Sort.by(sort)));
        List<UserInforResponse> listUserInforResponse = new ArrayList<>();


        users.forEach(user -> {
            try {
                Department department = departmentService.getDepartmentById(user.getDepartment() == null ? 0 : user.getDepartment().getId());
                Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
                ArrayList<Execute> executes = executeService.getListExecuteByUserId(user.getId());
                if (roles.contains("ROLE_USER")) {
                    UserInforResponse userInforResponse = UserInforResponse.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .fullName(user.getFullName())
                            .avatarUrl(user.getAvatarImage())
                            .dob(user.getDob()).gender(user.getGender())
                            .star(user.getStar()).roles(roles)
                            .department(department == null ? null
                                    : new UserInforResponse().departmentResponse(department.getId(), department.getName()))
                            .projects(new UserInforResponse().projectResponses(executes)).build();

                    listUserInforResponse.add(userInforResponse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(listUserInforResponse).build()
        );
    }

    @Override
    public ResponseEntity<?> putUserInformationById(UserInforResponse userInfo, String jwtToken) throws Exception {
        Optional<User> currentUser = userRepository.findById(userInfo.getId());

        if (currentUser.get() != null) {
            currentUser.get().setFullName(userInfo.getFullName());
            userRepository.save(currentUser.get());
        }
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(userRepository.findById(userInfo.getId())).build()
        );
    }

    @Override
    public ResponseEntity<?> isActiveUserById(long id, boolean isActive, String jwtToken) throws Exception {
        Optional<User> currentUser = userRepository.findById(id);

        if (currentUser.get() != null) {
            currentUser.get().setActive(isActive);
            userRepository.save(currentUser.get());
        }
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(userRepository.findById(id).get().isActive()).build()
        );
    }

    @Override
    public ResponseEntity<?> searchByName(String name, int page, int size, String sort, String jwtToken) throws Exception {
        Optional<Role> roleSatff = roleRepository.findById(Long.valueOf(5));
        List<User> listUser = userRepository.
                findByFullNameContainsAndRoles(name, roleSatff.get(), PageRequest.of(page, size, Sort.by(sort)));
        List<UserInforResponse> listUserInforResponse = new ArrayList<>();


        listUser.forEach(user -> {
            try {
                Department department = departmentService.getDepartmentById(user.getDepartment() == null ? 0 : user.getDepartment().getId());
                Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
                ArrayList<Execute> executes = executeService.getListExecuteByUserId(user.getId());

                UserInforResponse userInforResponse = UserInforResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .avatarUrl(user.getAvatarImage())
                        .dob(user.getDob()).gender(user.getGender())
                        .star(user.getStar()).roles(roles)
                        .department(department == null ? null
                                : new UserInforResponse().departmentResponse(department.getId(), department.getName()))
                        .projects(new UserInforResponse().projectResponses(executes)).build();

                listUserInforResponse.add(userInforResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(listUserInforResponse).build()
        );
    }
}
