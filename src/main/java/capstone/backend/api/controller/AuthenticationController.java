package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.*;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
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

    private AuthenticationService authenticationService;

    private CommonProperties commonProperties;

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
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_AUTH_FAILED())
                            .message(commonProperties.getMESSAGE_AUTH_FAILED()).build()
            );
        } catch (Exception e){
            logger.error("Undefined error");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Đăng ký")
    @PostMapping("/signup")
    public ResponseEntity<?> register(
            @ApiParam(value = "Thông tin đăng ký một tài khoản", required = true)
            @Valid @RequestBody UserRegisterDto userRegisterDto) {
        try {
            return authenticationService.register(userRegisterDto);
        } catch (Exception e) {
            logger.error("Register failed for user: " + userRegisterDto.getEmail());
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Lấy mã xác thực")
    @GetMapping("/get-verify-code")
    public ResponseEntity<?> getVerifyCode(
            @ApiParam(value = "Địa chỉ để gửi mail", required = true)
            @Valid @Param("email") String email) {
        try {
            return authenticationService.getVerifyCode(email);
        } catch (Exception e) {
            logger.error("Send code failed!");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Xác thực bằng mã")
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(
            @ApiParam(value = "Mã xác thực", required = true)
            @Valid @RequestBody VerifyCodeDto verifyCodeDto) {
        try {
            return authenticationService.verifyCode(verifyCodeDto);
        } catch (Exception e) {
            logger.error("verify code failed!");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Reset password")
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @ApiParam(value = "Password cũ và mới", required = true)
            @Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        try {
            return authenticationService.resetPassword(resetPasswordDto);
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
