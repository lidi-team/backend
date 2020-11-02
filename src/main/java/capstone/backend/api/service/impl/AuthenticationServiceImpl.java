package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.UserLoginDto;
import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.Role;
import capstone.backend.api.entity.User;
import capstone.backend.api.entity.security.TokenResponseInfo;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.AuthenticationService;
import capstone.backend.api.utils.DateUtils;
import capstone.backend.api.utils.RoleUtils;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final CommonProperties commonProperties;

    private final MailServiceImpl mailService;

    private final AuthenticationManager authenticationManager;

    private final capstone.backend.api.utils.StringUtils stringUtils;

    @Override
    public ResponseEntity<?> authenticate(UserLoginDto userLoginDto) throws AuthenticationException {
        if (StringUtils.isEmpty(userLoginDto.getEmail().trim())
                || StringUtils.isEmpty(userLoginDto.getPassword().trim())) {
            logger.error("Parameter invalid!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }

        TokenResponseInfo tokenResponseInfo = jwtUtils.generateTokenResponseInfo(userLoginDto.getEmail(), userLoginDto.getPassword(), authenticationManager);

        logger.info("authenticate Ok!");
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(tokenResponseInfo).build()
        );

    }

    @Override
    public ResponseEntity<?> getVerifyCode(String email) throws Exception {
        if (StringUtils.isEmpty(email.trim())) {
            logger.error("Parameter invalid!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            logger.error("Email is not correct");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_INVALID())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_INVALID()).build()
            );
        }


        String verifyCode = stringUtils.generateRandomCode(commonProperties.getCodeSize());

        try {
            mailService.CreateMailVerifyCode(email, verifyCode);
        } catch (Exception ignored) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_TIME_OUT())
                            .message("Cannot send mail!").build()
            );
        }

        String newPassword = passwordEncoder.encode(verifyCode);
        user.setPassword(newPassword);
        userRepository.save(user);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS()).build()
        );
    }



}
