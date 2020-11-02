package capstone.backend.api.service;

import capstone.backend.api.dto.UserLoginDto;
import capstone.backend.api.dto.UserRegisterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    ResponseEntity<?> authenticate(UserLoginDto userLoginDto) throws AuthenticationException;

    ResponseEntity<?> getVerifyCode(String email) throws Exception;
}
