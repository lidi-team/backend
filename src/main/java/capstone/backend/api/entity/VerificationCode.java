package capstone.backend.api.entity;

import lombok.*;

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
    private boolean active = true;

}
