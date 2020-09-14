package capstone.backend.api.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserChangePasswordDto {
    @NotNull
    @Email
    String email;
    @NotNull
    String oldPassword;
    @NotNull
    String newPassword;
}
