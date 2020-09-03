package capstone.backend.api.controller;

import capstone.backend.api.dto.UserChangePasswordDto;
import capstone.backend.api.dto.UserLoginDto;
import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/common/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@Valid @RequestBody UserLoginDto userLoginDto) {
        try {
            return authenticationService.authenticate(userLoginDto);
        } catch (AuthenticationException e) {
            logger.error("Authenticate failed for email: " + userLoginDto.getEmail());
            logger.error(e.getMessage());
            return ResponseEntity.status(401).body(
                    ApiResponse.builder().code(401).message("Authenticate failed!").build()
            );
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        try {
            return authenticationService.register(userRegisterDto);
        } catch (AuthenticationException e) {
            logger.error("Register failed for user: " + userRegisterDto.getEmail());
            logger.error(e.getMessage());
            return ResponseEntity.status(401).body(
                    ApiResponse.builder().code(401).message("Register failed!").build()
            );
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserChangePasswordDto userPassDto) {
        try {
            return authenticationService.changePassword(userPassDto);
        } catch (AuthenticationException e) {
            logger.error("change password failed for user: " + userPassDto.getEmail());
            logger.error(e.getMessage());
            return ResponseEntity.status(401).body(
                    ApiResponse.builder().code(401).message("Change password failed!").build()
            );
        }
    }
    @PostMapping("/get-verify-code")
    public ResponseEntity<?> getVerifyCode(@Valid @RequestBody String email){
        try {
            return null;
        } catch (AuthenticationException e) {
            logger.error("Send code failed!");
            logger.error(e.getMessage());
            return ResponseEntity.status(401).body(
                    ApiResponse.builder().code(401).message("Send code failed!").build()
            );
        }
    }
}
