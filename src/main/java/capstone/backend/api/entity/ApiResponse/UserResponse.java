package capstone.backend.api.entity.ApiResponse;

import capstone.backend.api.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private long id;

    private String fullName;

    private String email;

    private Set<String> roles;

    private String avatarUrl;
}
