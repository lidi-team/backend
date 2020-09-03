package capstone.backend.api.service;

import capstone.backend.api.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    public ResponseEntity<?> getUserByEmail(String email);
}
