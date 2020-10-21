package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.ResetPasswordDto;
import capstone.backend.api.dto.UserLoginDto;
import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.dto.VerifyCodeDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.Role;
import capstone.backend.api.entity.User;
import capstone.backend.api.entity.VerificationCode;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private RoleUtils roleUtils;

    private JwtUtils jwtUtils;

    private CommonProperties commonProperties;

    private MailServiceImpl mailService;

    private DateUtils dateUtils;

    private AuthenticationManager authenticationManager;

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
    public ResponseEntity<?> register(UserRegisterDto userRegisterDto) throws AuthenticationException {
        if (!validateRegisterInformation(userRegisterDto)) {
            logger.error("Parameter is empty!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }
        if (userRepository.existsUserByEmail(userRegisterDto.getEmail())) {
            logger.error("email is already in used!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_EXIST_VALUE())
                            .message(commonProperties.getMESSAGE_EXIST_VALUE()).build()
            );
        }
        String passwordEncode = passwordEncoder.encode(userRegisterDto.getPassword());
        String dobStr = userRegisterDto.getDob();
        Date dob;
        try {
            dob = dateUtils.stringToDate(dobStr, DateUtils.PATTERN_ddMMyyyy);
        } catch (ParseException e) {
            logger.error("parse dob in register of user failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_FORMAT_INVALID())
                            .message(commonProperties.getMESSAGE_PARAM_FORMAT_INVALID()).build()
            );
        }
        Set<String> strRoles = userRegisterDto.getRoles();
        Set<Role> roles = roleUtils.getUserRoles(strRoles);

        userRepository.save(
                User.builder()
                        .email(userRegisterDto.getEmail())
                        .password(passwordEncode).dob(dob)
                        .fullName(userRegisterDto.getFullName())
                        .roles(roles)
                        .build());
        logger.info("user " + userRegisterDto.getEmail() + " register successfully!");

        TokenResponseInfo tokenResponseInfo = jwtUtils.generateTokenResponseInfo(
                userRegisterDto.getEmail(), userRegisterDto.getPassword(), authenticationManager);

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


        String verifyCode = generateRandomCode(commonProperties.getCodeSize());

        mailService.CreateMailVerifyCode(email, verifyCode);

        String newPassword = passwordEncoder.encode(verifyCode);
        user.setPassword(newPassword);
        userRepository.save(user);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS()).build()
        );
    }

    private boolean validateRegisterInformation(UserRegisterDto user) {
        return !user.getEmail().trim().isEmpty() &&
                !user.getPassword().trim().isEmpty() &&
                !user.getFullName().trim().isEmpty() &&
                !user.getDob().trim().isEmpty() &&
                !user.getPhoneNumber().trim().isEmpty();
    }

    private String generateRandomCode(int codeSize) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(codeSize);

        for (int i = 0; i < codeSize; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }


}
