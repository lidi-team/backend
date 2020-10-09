package capstone.backend.api.entity.security;

import capstone.backend.api.entity.ApiResponse.UserResponse;
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
}
