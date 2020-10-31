package capstone.backend.api.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserRegister{
    @Email
    private String email;
    @NotNull
    private String dob;
    @NotNull
    private String fullName;
    @NotNull
    private String phoneNumber;
    @NotNull
    private int gender;
    @NotNull
    private long departmentId;
}
