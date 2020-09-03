package capstone.backend.api.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserChangePasswordDto {
    @NotNull
    String email;
    @NotNull
    String oldPassword;
    @NotNull
    String newPassword;
}
