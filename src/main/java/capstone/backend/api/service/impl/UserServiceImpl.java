package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.UserChangePasswordDto;
import capstone.backend.api.dto.UserRegister;
import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.User.UserFailResponse;
import capstone.backend.api.entity.ApiResponse.User.UserFailedItem;
import capstone.backend.api.entity.ApiResponse.User.UserInforResponse;
import capstone.backend.api.entity.Department;
import capstone.backend.api.entity.Execute;
import capstone.backend.api.entity.Role;
import capstone.backend.api.entity.User;
import capstone.backend.api.repository.RoleRepository;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.UserService;
import capstone.backend.api.utils.DateUtils;
import capstone.backend.api.utils.RoleUtils;
import capstone.backend.api.utils.StringUtils;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final CommonProperties commonProperties;

    private final RoleRepository roleRepository;

    private final JwtUtils jwtUtils;

    private final DepartmentServiceImpl departmentService;

    private final ExecuteServiceImpl executeService;

    private final RoleUtils roleUtils;

    private final PasswordEncoder passwordEncoder;

    private final DateUtils dateUtils;

    private final StringUtils stringUtils;

    private final MailServiceImpl mailService;

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

        Department department = departmentService.getDepartmentById(user.getDepartment().getId());
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
        List<UserInforResponse> listUserInforResponse = setUserInformation(users);
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
        List<UserInforResponse> listUserInforResponse = setUserInformation(users);
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(listUserInforResponse.size()).build()
        );
    }

    @Override
    public ResponseEntity<?> getAllUsers(String name, int page, int size, String sort, String jwtToken) throws Exception {
        List<User> users = userRepository.
                findByFullNameContains(name, PageRequest.of(page, size, Sort.by(sort)));
//        List<User> users = userRepository.findAll(PageRequest.of(page, size, Sort.by(sort).ascending())).toList();
        int count = userRepository.findByFullNameContains(name).size();
//        List<UserInforResponse> listUserInforResponse = setUserInformation(users);
        List<Object> listUser = new ArrayList<>();

        users.forEach(user -> {
            try {
                listUser.add(customUserInformation(user));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Map<String, Object> response = new HashMap<>();
        response.put("data", listUser);
        Map<String, Object> meta = new HashMap<>();
        meta.put("totalItems", count);
        meta.put("totalPages", (count%size == 0 ? count/size : count/size + 1 ));
        response.put("meta", meta);

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    @Override
    public ResponseEntity<?> getStaffPaging(String name, int page, int size, String sort, String jwtToken) throws Exception {
        Optional<Role> roleStaff = roleRepository.findById(5L);

        List<User> users = userRepository.
                findByFullNameContainsAndRoles(name, roleStaff.get(), PageRequest.of(page, size, Sort.by(sort)));
//        List<User> users = userRepository.findAllByRolesContains(roleStaff.get(), PageRequest.of(page, size, Sort.by(sort)));
        int count = userRepository.findByFullNameContainsAndRoles(name, roleStaff.get()).size();
//        List<UserInforResponse> listUserInforResponse = setUserInformation(users);

        List<Object> listUser = new ArrayList<>();

        users.forEach(user -> {
            try {
                listUser.add(customUserInformation(user));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Map<String, Object> response = new HashMap<>();
        response.put("data", listUser);
        Map<String, Object> meta = new HashMap<>();
        meta.put("totalItems", count);
        meta.put("totalPages", (count%size == 0 ? count/size : count/size + 1 ));
        response.put("meta", meta);
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    @Override
    public ResponseEntity<?> putUserInformationById(UserInforResponse userInfo, String jwtToken) throws Exception {
        User currentUser = userRepository.findById(userInfo.getId()).orElse(null);

        if (currentUser != null) {
            currentUser.setFullName(userInfo.getFullName());
            userRepository.save(currentUser);
        }
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(userRepository.findById(userInfo.getId())).build()
        );
    }

    @Override
    public ResponseEntity<?> isActiveUserById(long id, boolean isActive, String jwtToken) throws Exception {
        User currentUser = userRepository.findById(id).orElse(null);

        if (currentUser != null) {
            currentUser.setActive(isActive);
            userRepository.save(currentUser);
        }
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(userRepository.findById(id).get().isActive()).build()
        );
    }

    @Override
    public ResponseEntity<?> searchByName(String name, int page, int size, String sort, String jwtToken) throws Exception {
        Role roleStaff = roleRepository.findById(5L).get();
        List<User> listUser = userRepository.
                findByFullNameContainsAndRoles(name, roleStaff, PageRequest.of(page, size, Sort.by(sort)));
        int count = userRepository.findByFullNameContainsAndRoles(name, roleStaff).size();
        List<UserInforResponse> listUserInforResponse = setUserInformation(listUser);

        Map<String, Object> response = new HashMap<>();
        response.put("data", listUser);
        Map<String, Object> meta = new HashMap<>();
        meta.put("totalItems", count);
        meta.put("totalPages", (count%size == 0 ? count/size : count/size + 1 ));
        response.put("meta", meta);

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    private Map<String, Object> customUserInformation(User user) throws Exception{
        Map<String, Object> userCustom = new HashMap<>();

            try {
                Department department = departmentService.getDepartmentById(user.getDepartment() == null ? 0 : user.getDepartment().getId());
                Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
                ArrayList<Execute> executes = executeService.getListExecuteByUserId(user.getId());

                userCustom.put("id", user.getId());
                userCustom.put("email", user.getEmail());
                userCustom.put("fullName", user.getFullName());
                userCustom.put("avatarUrl", user.getAvatarImage());
                userCustom.put("dob", user.getDob());
                userCustom.put("gender", user.getGender());
                userCustom.put("star", user.getStar());
                userCustom.put("roles", roles);
                userCustom.put("department", department == null ? null : new UserInforResponse().departmentResponse(department.getId(), department.getName()));
                userCustom.put("projects", new UserInforResponse().projectResponses(executes));
                userCustom.put("isActive", user.isActive());
            } catch (Exception e) {
                e.printStackTrace();
            }

        return userCustom;
    }

    private List<UserInforResponse> setUserInformation(List<User> users) throws Exception {
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

        return listUserInforResponse;
    }

    @Override
    public ResponseEntity<?> addListUsers(UserRegisterDto userDto) throws Exception {
        HashMap<User, String> map = new HashMap<>();
        List<UserRegister> userRegisters = userDto.getUsers();
        List<UserFailedItem> fails = new ArrayList<>();
        List<User> successes = new ArrayList<>();

        divineList(successes, fails, userRegisters, map);

        userRepository.saveAll(successes);

        UserFailResponse failResponse = UserFailResponse.builder()
                .numberOfSuccess(successes.size())
                .numberOfFailed(fails.size())
                .list(fails)
                .build();

        (new Thread(() -> {
            try {
                mailService.sendWelcomeEmail(map);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        })).start();

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(failResponse)
                        .build()
        );
    }

    private String validateRegisterInformation(UserRegister user) {
        if (user.getEmail().trim().isEmpty()) {
            return "Email bị để trống!";
        }
        if (user.getFullName().trim().isEmpty()) {
            return "Họ và tên bị để trống!";
        }
        if (user.getDob().trim().isEmpty()) {
            return "Ngày sinh bị để trống!";
        }
        if (user.getPhoneNumber().trim().isEmpty()) {
            return "Số điện thoại bị để trống!";
        }
        return "";
    }

    private void divineList(List<User> users, List<UserFailedItem> fails,
                            List<UserRegister> userRegisters, HashMap<User, String> map) {
        for (UserRegister userRegister : userRegisters) {
            try {
                String validate = validateRegisterInformation(userRegister);
                if (!validate.equals("")) {
                    fails.add(
                            UserFailedItem.builder()
                                    .email(userRegister.getEmail())
                                    .reason(validate)
                                    .departmentId(userRegister.getDepartmentId())
                                    .dob(userRegister.getDob())
                                    .fullName(userRegister.getFullName())
                                    .gender(userRegister.getGender())
                                    .phoneNumber(userRegister.getPhoneNumber())
                                    .build()
                    );
                    continue;
                }
                if (userRepository.existsUserByEmail(userRegister.getEmail())) {
                    fails.add(
                            UserFailedItem.builder()
                                    .email(userRegister.getEmail())
                                    .reason("Email đã được sử dụng!")
                                    .departmentId(userRegister.getDepartmentId())
                                    .dob(userRegister.getDob())
                                    .fullName(userRegister.getFullName())
                                    .gender(userRegister.getGender())
                                    .phoneNumber(userRegister.getPhoneNumber())
                                    .build()
                    );
                    continue;
                }
                Department department = departmentService.getDepartmentById(userRegister.getDepartmentId());
                if (department == null) {
                    fails.add(
                            UserFailedItem.builder()
                                    .email(userRegister.getEmail())
                                    .reason("Không tìm thấy Đơn vị!")
                                    .departmentId(userRegister.getDepartmentId())
                                    .dob(userRegister.getDob())
                                    .fullName(userRegister.getFullName())
                                    .gender(userRegister.getGender())
                                    .phoneNumber(userRegister.getPhoneNumber())
                                    .build()
                    );
                    continue;
                }
                String password = stringUtils.generateRandomCode(commonProperties.getCodeSize());
                String passwordEncode = passwordEncoder.encode(password);
                String dobStr = userRegister.getDob();
                Date dob = dateUtils.stringToDate(dobStr, DateUtils.PATTERN_ddMMyyyy);
                Set<Role> roles = roleUtils.getUserRoles(null);
                String url = "";
                User user = User.builder()
                        .email(userRegister.getEmail())
                        .fullName(userRegister.getFullName())
                        .dob(dob)
                        .password(passwordEncode)
                        .gender(userRegister.getGender())
                        .roles(roles)
                        .avatarImage(url)
                        .createAt(new Date())
                        .department(department)
                        .star(0)
                        .build();
                users.add(user);
                map.put(user, password);
            } catch (Exception e) {
                fails.add(
                        UserFailedItem.builder()
                                .email(userRegister.getEmail())
                                .departmentId(userRegister.getDepartmentId())
                                .dob(userRegister.getDob())
                                .fullName(userRegister.getFullName())
                                .gender(userRegister.getGender())
                                .phoneNumber(userRegister.getPhoneNumber())
                                .build()
                );
            }
        }
    }
}
