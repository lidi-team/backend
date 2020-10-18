package capstone.backend.api.entity.security;

import capstone.backend.api.service.impl.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponseInfo {
    private String jwtToken;
    private UserResponse user;

    public UserResponse userResponse(UserDetailsImpl userDetails, Set<String> roles) {
        return UserResponse.builder()
                .id(userDetails.getId())
                .fullName(userDetails.getFullName())
                .email(userDetails.getUsername())
                .avatarUrl(userDetails.getAvatarUrl())
                .roles(roles).build();
    }
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UserResponse {

    private long id;

    private String fullName;

    private String email;

    private Set<String> roles;

    private String avatarUrl;
}