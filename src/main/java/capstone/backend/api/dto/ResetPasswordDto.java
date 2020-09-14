package capstone.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class ResetPasswordDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String resetCode;
    @NotNull
    private String newPassword;
}
