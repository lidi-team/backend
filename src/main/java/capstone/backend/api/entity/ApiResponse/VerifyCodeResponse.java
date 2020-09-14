package capstone.backend.api.entity.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class VerifyCodeResponse {
    @NotNull
    private String resetCode;
}
