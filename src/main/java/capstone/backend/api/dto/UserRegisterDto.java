package capstone.backend.api.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
public class UserRegisterDto {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String dob;
    @NotNull
    private String fullName;
    @NotNull
    private String phoneNumber;
    @NotNull
    private int gender;

    private Set<String> roles;
}
