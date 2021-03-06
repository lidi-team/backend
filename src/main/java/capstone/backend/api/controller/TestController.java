package capstone.backend.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/test")
public class TestController {

//    @GetMapping("teacherRegister")
//    public UserRegisterDto teacherRegisterApi() {
//        Set<String> roles = new HashSet<>();
//        roles.add(RoleUtils.ROLE_USER);
//        return UserRegisterDto.builder()
//                .email("hoang5.com")
//                .password("123445").dob("22/11/1998")
//                .fullName("Le Son Tung")
//                .phoneNumber("0342529999")
//                .gender(1)
//                .roles(roles).build();
//    }
//
//    @GetMapping("studentRegister")
//    public UserRegisterDto studentRegisterApi() {
//        Set<String> roles = new HashSet<>();
//        roles.add(RoleUtils.ROLE_USER);
//        return UserRegisterDto.builder()
//                .email("nguyenminhchau@gmail.com")
//                .password("mothaiba").dob("12/11/1998")
//                .fullName("Nguyen Minh Chau")
//                .phoneNumber("0369829999")
//                .gender(0)
//                .roles(roles).build();
//    }

   @GetMapping("student")
    public String userApi() {
        return "User API";
    }
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    // @GetMapping("admin")
    // public String adminApi() {
    //     return "Admin API";
    // }

    // @PreAuthorize("hasRole('ROLE_USER')")
    // @GetMapping("student")
    // public String userApi() {
    //     return "User API";
    // }
}
