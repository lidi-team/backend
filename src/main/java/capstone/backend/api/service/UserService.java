package capstone.backend.api.service;

import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    ResponseEntity<?> getUserByEmail(String email);

    ResponseEntity<?> getAllUsers(int size, int page);

    ResponseEntity<?> getAllUsersByDepartmentId(long id, int page, int size);

    ResponseEntity<?> getUserInformation(String jwtToken) throws Exception;
}
