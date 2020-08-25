package capstone.backend.base.DTO;

import lombok.Data;

import java.util.Set;

@Data
public class UserRegisterDto {
    String username;
    String password;
    String fullName;
    int age;
    Set<String> roles;
}
