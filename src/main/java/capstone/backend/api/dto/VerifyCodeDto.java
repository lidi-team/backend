package capstone.backend.api.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class VerifyCodeDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    private String verifyCode;
}
