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
@Api(value = "Ðang nh?p, xác th?c")
@AllArgsConstructor
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private AuthenticationService authenticationService;

    private CommonProperties commonProperties;

    @ApiOperation(value = "Ðang nh?p")
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(
            @ApiParam(value = "Thông tin dang nh?p", required = true)
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

    @ApiOperation(value = "Ðang ký")
    @PostMapping("/signup")
    public ResponseEntity<?> register(
            @ApiParam(value = "Thông tin dang ký m?t tài kho?n", required = true)
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

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam(name = "email") String email) {
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
