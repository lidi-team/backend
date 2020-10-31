package capstone.backend.api.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserRegisterDto {
    private List<UserRegister> users;

}
