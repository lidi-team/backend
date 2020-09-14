package capstone.backend.api.controller;

import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.utils.RoleUtils;
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
        roles.add(RoleUtils.ROLE_STUDENT);
        roles.add(RoleUtils.ROLE_TEACHER);
        return UserRegisterDto.builder()
                .email("sontung199x@gmail.com")
                .password("123445").dob("22/11/1998")
                .fullName("Le Son Tung")
                .phoneNumber("0342529999")
                .gender("male")
                .roles(roles).build();
    }

    @GetMapping("studentRegister")
    public UserRegisterDto studentRegisterApi() {
        Set<String> roles = new HashSet<>();
        roles.add(RoleUtils.ROLE_STUDENT);
        return UserRegisterDto.builder()
                .email("nguyenminhchau@gmail.com")
                .password("mothaiba").dob("12/11/1998")
                .fullName("Nguyen Minh Chau")
                .phoneNumber("0369829999")
                .gender("female")
                .roles(roles).build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("admin")
    public String adminApi() {
        return "Admin API";
    }

    @PreAuthorize("hasRole('ROLE_STUDENT')")
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
