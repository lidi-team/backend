package capstone.backend.api.entity.security;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class TokenResponseInfo {
    private String jwtToken;
    private String email;
    private Set<String> roles;
}
