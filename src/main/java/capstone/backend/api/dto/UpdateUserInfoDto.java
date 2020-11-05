package capstone.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoDto {
    @Email
    private String email;
    @NotNull
    private String dob;
    @NotNull
    private String fullName;
    @NotNull
    private int gender;
    @NotNull
    private long departmentId;

    private boolean isActive;
}
