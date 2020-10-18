package capstone.backend.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationCode {
    @NotNull
    private String verifyCode;

    @NotNull
    @Future
    private Date expiredTime;

    @NotNull
    private String resetCode;

    @NotNull
    private User user;

    @NotNull
    @Builder.Default
    private boolean active = true;

}
