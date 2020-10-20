package capstone.backend.api.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    ResponseEntity<?> getAllRoles(String jwtToken) throws Exception;
}
