package capstone.backend.api.service.impl;

import capstone.backend.api.entity.User;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public ResponseEntity<?> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }
}
