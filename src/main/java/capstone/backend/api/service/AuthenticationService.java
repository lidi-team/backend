package capstone.backend.api.service;

import capstone.backend.api.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
@Service
public interface AuthenticationService {
    ResponseEntity<?> authenticate(UserLoginDto userLoginDto) throws AuthenticationException;

    ResponseEntity<?> register(UserRegisterDto userRegisterDto) throws AuthenticationException;

    ResponseEntity<?> changePassword(UserChangePasswordDto userChangePasswordDto) throws AuthenticationException;

    ResponseEntity<?> getVerifyCode(String email) throws AuthenticationException, MessagingException;

    ResponseEntity<?> verifyCode(VerifyCodeDto verifyCodeDto) throws Exception;

    ResponseEntity<?> resetPassword(ResetPasswordDto resetPass) throws Exception;
}
