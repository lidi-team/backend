package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.UserLoginDto;
import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Api(value = "Đăng nhập, xác thực")
@AllArgsConstructor
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    private final CommonProperties commonProperties;

    @ApiOperation(value = "Đăng nhập")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(
            @ApiParam(value = "Thông tin đăng nhập", required = true)
            @Valid @RequestBody UserLoginDto userLoginDto) {
        try {
            return authenticationService.authenticate(userLoginDto);
        } catch (AuthenticationException e) {
            logger.error("Authenticate failed for email: " + userLoginDto.getEmail());
            logger.error(e.getMessage());
            return ResponseEntity.status(401).body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message("Email hoặc mật khẩu không chính xác").build()
            );
        } catch (Exception e) {
            logger.error("Undefined error");
            logger.error(e.getMessage());
            return ResponseEntity.status(401).body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Reset lại mật khẩu")
    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @ApiParam(value = "Thông tin email để gửi mật khẩu mới", required = true)
            @RequestParam(name = "email") String email) {
        try {
            return authenticationService.getVerifyCode(email);
        } catch (Exception e) {
            logger.error("Reset password failed!");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}
