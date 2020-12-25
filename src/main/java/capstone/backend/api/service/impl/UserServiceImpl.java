package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.UpdateUserInfoDto;
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
import capstone.backend.api.repository.DepartmentRepository;
import capstone.backend.api.repository.RoleRepository;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.UserService;
import capstone.backend.api.utils.CommonUtils;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import javax.mail.MessagingException;
import java.text.ParseException;
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

    private final CommonUtils utils;

    private final PasswordEncoder passwordEncoder;

    private final MailServiceImpl mailService;

    private final DepartmentRepository departmentRepository;

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
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }

        String email = jwtUtils.getUserNameFromJwtToken(jwtToken.substring(5));
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        if (!passwordEncoder.matches(userPassDto.getOldPassword(), user.getPassword())) {
            logger.error("Old password is incorrect!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_INVALID()).build()
            );
        }

        String newPassword = passwordEncoder.encode(userPassDto.getNewPassword());
        user.setPassword(newPassword);
        userRepository.save(user);
        logger.info("update successful!");

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS()).build()
        );
    }

    @Override
    public ResponseEntity<?> saveAvatarLink(String url, String token) throws Exception {
        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                            .message("Không thể cập nhật ảnh đại diện").build()
            );
        }
        user.setAvatarImage(url);

        userRepository.save(user);

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message("Cập nhật avatar thành công").build()
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
                            .message("").build()
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
        if(page < 1){
            page = 1;
        }
        if(size <= 0){
            size = 10;
        }
        Page<User> users = userRepository.
                findByFullNameContains(name, PageRequest.of(page-1, size, Sort.by(sort)));

        List<Object> listUser = new ArrayList<>();

        users.getContent().forEach(user -> {
            try {
                listUser.add(customUserInformation(user));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Map<String, Object> response = new HashMap<>();
        response.put("data", listUser);
        Map<String, Object> meta = new HashMap<>();
        meta.put("totalItems", users.getTotalElements());
        meta.put("totalPages", users.getTotalPages());
        response.put("meta", meta);

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    @Override
    public ResponseEntity<?> getStaffPaging(String name, int page, int size, String sort, String jwtToken) throws Exception {
        if(page < 1){
            page = 1;
        }
        if(size <= 0){
            size = 10;
        }
        Role roleStaff = roleRepository.findRoleByName("ROLE_USER").orElse(null);

        if(roleStaff == null){
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }
        List<User> users = userRepository.
                findByFullNameContainsAndRoles(name, roleStaff);
        List<User> staffs = users.stream().filter(user -> (user.getRoles().size() == 1 && user.getRoles().contains(roleStaff))).collect(Collectors.toList());

//        PageImpl<User> pages = new PageImpl<User>(staffs,PageRequest.of(page - 1, size),staffs.size());
        PagedListHolder pages = new PagedListHolder(staffs);
        pages.setPageSize(size); // number of items per page
        pages.setPage(page - 1);      // set to first page

        pages.getPageCount(); // number of pages
        pages.getPageList();

        List<Object> listUser = new ArrayList<>();
        for (Object o : pages.getPageList()) {
            User user = (User) o;
            listUser.add(customUserInformation(user));
        }
        Map<String, Object> response = new HashMap<>();
        response.put("data", listUser);
        Map<String, Object> meta = new HashMap<>();
        meta.put("totalItems", staffs.size());
        meta.put("totalPages", pages.getPageCount());
        meta.put("currentPage",page);
        response.put("meta", meta);
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    @Override
    public ResponseEntity<?> putUserInformationById(UpdateUserInfoDto userInfo, long id) throws Exception {
        UserInforResponse userInforResponse = new UserInforResponse();
        User currentUser = userRepository.findById(id).orElse(null);

        if (currentUser != null) {
            Department department = departmentService.getDepartmentById(userInfo.getDepartmentId());
            Date date = utils.stringToDate(userInfo.getDob(), "dd/MM/yyyy");

            currentUser.setFullName(userInfo.getFullName());
            currentUser.setEmail(userInfo.getEmail());
            currentUser.setGender(userInfo.getGender());
            currentUser.setDob(date);
            currentUser.setDepartment(department);
            currentUser.setActive(userInfo.isActive());
            userRepository.save(currentUser);

            Set<String> roles = currentUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
            ArrayList<Execute> executes = executeService.getListExecuteByUserId(currentUser.getId());

            userInforResponse = UserInforResponse.builder().id(currentUser.getId())
                    .email(currentUser.getEmail())
                    .fullName(currentUser.getFullName())
                    .avatarUrl(currentUser.getAvatarImage())
                    .dob(currentUser.getDob()).gender(currentUser.getGender())
                    .star(currentUser.getStar()).roles(roles)
                    .department(department == null ? null
                            : new UserInforResponse().departmentResponse(department.getId(), department.getName()))
                    .projects(new UserInforResponse().projectResponses(executes)).build();
        }
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(userInforResponse).build()
        );
    }

    @Override
    public ResponseEntity<?> isActiveUserById(long id) throws Exception {
        User currentUser = userRepository.findById(id).orElse(null);

        if (currentUser == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message("Người dùng này không tồn tại").build()
            );
        }
        if (currentUser.isActive()) {
            for (Role role : currentUser.getRoles()) {
                if (role.getName().equalsIgnoreCase("ROLE_DIRECTOR")) {
                    return ResponseEntity.badRequest().body(
                            ApiResponse.builder()
                                    .code(commonProperties.getCODE_UPDATE_FAILED())
                                    .message("Người dùng này đang có quyền giám đốc").build()
                    );
                }

                if (role.getName().equalsIgnoreCase("ROLE_PM")) {
                    return ResponseEntity.badRequest().body(
                            ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                                    .message("Người dùng này đang là PM của dự án").build()
                    );
                }
                currentUser.setActive(false);
            }
        } else {
            currentUser.setActive(true);
        }
        userRepository.save(currentUser);

        String message = "";
        if (currentUser.isActive()) {
            message = "Đã active thành công người dùng";
        } else {
            message = "Đã de-active người dùng này";
        }
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message(message).build()
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
        meta.put("totalPages", (count % size == 0 ? count / size : count / size + 1));
        response.put("meta", meta);

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    private Map<String, Object> customUserInformation(User user) throws Exception {
        Map<String, Object> userCustom = new HashMap<>();

        try {Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
            ArrayList<Execute> executes = executeService.getListExecuteByUserId(user.getId());

            userCustom.put("id", user.getId());
            userCustom.put("email", user.getEmail());
            userCustom.put("fullName", user.getFullName());
            userCustom.put("avatarUrl", user.getAvatarImage());
            userCustom.put("dob", user.getDob());
            userCustom.put("gender", user.getGender());
            userCustom.put("star", user.getStar());
            userCustom.put("roles", roles);
            userCustom.put("department", user.getDepartment() == null ? null : new UserInforResponse().departmentResponse(user.getDepartment().getId(), user.getDepartment().getName()));
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
            try {Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
                ArrayList<Execute> executes = executeService.getListExecuteByUserId(user.getId());

                UserInforResponse userInforResponse = UserInforResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .avatarUrl(user.getAvatarImage())
                        .dob(user.getDob()).gender(user.getGender())
                        .star(user.getStar()).roles(roles)
                        .department(user.getDepartment() == null ? null
                                : new UserInforResponse().departmentResponse(user.getDepartment().getId(), user.getDepartment().getName()))
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


        UserFailResponse failResponse = UserFailResponse.builder()
                .numberOfSuccess(successes.size())
                .numberOfFailed(fails.size())
                .list(fails)
                .build();

        if (fails.isEmpty()) {
            userRepository.saveAll(successes);
            (new Thread(() -> {
                try {
                    mailService.sendWelcomeEmail(map);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            })).start();

            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_SUCCESS())
                            .message("Thêm người dùng thành công")
                            .build()
            );
        }

        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_UPDATE_FAILED())
                        .message("Thêm người dùng không thành công")
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
                                    .build()
                    );
                    continue;
                }
                String password = utils.generateRandomCode(commonProperties.getCodeSize());
                String passwordEncode = passwordEncoder.encode(password);
                String dobStr = userRegister.getDob();
                Date dob = utils.stringToDate(dobStr, CommonUtils.PATTERN_ddMMyyyy);
                Set<Role> roles = utils.getUserRoles(null);
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
            } catch (ParseException ex) {
                fails.add(
                        UserFailedItem.builder()
                                .email(userRegister.getEmail())
                                .departmentId(userRegister.getDepartmentId())
                                .dob(userRegister.getDob())
                                .fullName(userRegister.getFullName())
                                .gender(userRegister.getGender())
                                .reason("Định dạng ngày không hợp lệ!")
                                .build()
                );
            } catch (Exception e) {
                fails.add(
                        UserFailedItem.builder()
                                .email(userRegister.getEmail())
                                .departmentId(userRegister.getDepartmentId())
                                .dob(userRegister.getDob())
                                .fullName(userRegister.getFullName())
                                .gender(userRegister.getGender())
                                .build()
                );
            }
        }
    }
}
