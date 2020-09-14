package capstone.backend.api.service;

import capstone.backend.api.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
@Service
public interface AuthenticationService {
    public ResponseEntity<?> authenticate(UserLoginDto userLoginDto) throws AuthenticationException;

    public ResponseEntity<?> register(UserRegisterDto userRegisterDto) throws AuthenticationException;

    public ResponseEntity<?> changePassword(UserChangePasswordDto userChangePasswordDto) throws AuthenticationException;

    public ResponseEntity<?> getVerifyCode(String email) throws AuthenticationException, MessagingException;

    public ResponseEntity<?> verifyCode(VerifyCodeDto verifyCodeDto) throws Exception;

    public ResponseEntity<?> resetPassword(ResetPasswordDto resetPass) throws Exception;
}
