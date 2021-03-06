package capstone.backend.api.entity.ApiResponse.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFailedItem {
    private String email;
    @NotNull
    private String dob;
    @NotNull
    private String fullName;
    @NotNull
    private int gender;
    @NotNull
    private long departmentId;

    private String reason;
}
