package capstone.backend.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserRegisterDto {
    String username;
    String password;
    String fullName;
    int age;
    Set<String> roles;
}
