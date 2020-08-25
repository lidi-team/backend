package capstone.backend.base.service;

import capstone.backend.base.DTO.UserLoginDto;
import capstone.backend.base.DTO.UserRegisterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    public ResponseEntity<?> authenticate(UserLoginDto userLoginDto) throws AuthenticationException;

    public ResponseEntity<?> register(UserRegisterDto userRegisterDto) throws AuthenticationException;
}
