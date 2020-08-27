package capstone.backend.api.controller;

import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.utils.RoleConstants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

    @GetMapping("teacherRegister")
    public UserRegisterDto teacherRegisterApi() {
        Set<String> roles = new HashSet<>();
        roles.add(RoleConstants.ROLE_USER);
        roles.add(RoleConstants.ROLE_TEACHER);
        return UserRegisterDto.builder()
                .username("tungbeo")
                .password("123445")
                .age(23)
                .fullName("Le Son Tung")
                .roles(roles)
                .build();
    }

    @GetMapping("studentRegister")
    public UserRegisterDto studentRegisterApi() {
        Set<String> roles = new HashSet<>();
        roles.add(RoleConstants.ROLE_USER);
        return UserRegisterDto.builder()
                .username("hoangtubongdem")
                .password("123456")
                .age(23)
                .fullName("Nguyen Van Troi")
                .roles(roles)
                .build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("admin")
    public String adminApi() {
        return "Admin API";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("student")
    public String userApi() {
        return "Student API";
    }

    @PreAuthorize("hasRole('ROLE_TEACHER')")
    @GetMapping("teacher")
    public String teacherApi() {
        return "Teacher API";
    }
}
