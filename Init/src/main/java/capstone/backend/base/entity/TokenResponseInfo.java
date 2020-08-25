package capstone.backend.base.entity;

import lombok.AllArgsConstructor;
import org.eclipse.xtend.lib.annotations.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class TokenResponseInfo {
    private String jwtToken;
    private String username;
    private Set<String> roles;
}
