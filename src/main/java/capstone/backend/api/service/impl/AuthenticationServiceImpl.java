package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.*;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.VerifyCodeResponse;
import capstone.backend.api.entity.Role;
import capstone.backend.api.entity.User;
import capstone.backend.api.entity.VerificationCode;
import capstone.backend.api.entity.security.TokenResponseInfo;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.AuthenticationService;
import capstone.backend.api.service.impl.security.UserDetailsImpl;
import capstone.backend.api.utils.DateUtils;
import capstone.backend.api.utils.RoleUtils;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private RoleUtils roleUtils;

    private JwtUtils jwtUtils;

    private CommonProperties commonProperties;

    private MailServiceImpl mailService;

    private ArrayList<VerificationCode> verificationList;

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

        TokenResponseInfo tokenResponseInfo = generateTokenResponseInfo(userLoginDto.getEmail(), userLoginDto.getPassword());

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
            dob = DateUtils.stringToDate(dobStr, DateUtils.PATTERN_ddMMyyyy);
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

        TokenResponseInfo tokenResponseInfo = generateTokenResponseInfo(userRegisterDto.getEmail(), userRegisterDto.getPassword());

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(tokenResponseInfo).build()
        );
    }

    @Override
    public ResponseEntity<?> changePassword(UserChangePasswordDto userPassDto) throws AuthenticationException {
        if (!validateChangePasswordInformation(userPassDto)) {
            logger.error("Parameter invalid!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }

        User user = userRepository.findByEmail(userPassDto.getEmail()).get();

        if (!passwordEncoder.matches(userPassDto.getOldPassword(), user.getPassword())) {
            logger.error("Old password is incorrect!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_INVALID())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_INVALID()).build()
            );
        }

        String newPassword = passwordEncoder.encode(userPassDto.getNewPassword());
        user.setPassword(newPassword);
        userRepository.save(user);
        logger.info("update successful!");

        TokenResponseInfo tokenResponseInfo = generateTokenResponseInfo(userPassDto.getEmail(), userPassDto.getNewPassword());

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(tokenResponseInfo).build()
        );
    }

    @Override
    public ResponseEntity<?> getVerifyCode(String email) throws AuthenticationException, MessagingException {
        if (StringUtils.isEmpty(email.trim())) {
            logger.error("Parameter invalid!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }

        String verifyCode = generateRandomIntegerCode(commonProperties.getCodeSize());
        String resetCode = generateRandomIntegerCode(commonProperties.getResetCodeSize());
        Date expiredDate = new Date((new Date()).getTime() + commonProperties.getExpiredTime());

        mailService.CreateMailVerifyCode(email, verifyCode);

        User user = userRepository.findByEmail(email).get();
        verificationList.add(
                VerificationCode.builder()
                        .verifyCode(passwordEncoder.encode(verifyCode))
                        .expiredTime(expiredDate)
                        .resetCode(passwordEncoder.encode(resetCode))
                        .user(user)
                        .active(true).build()
        );
        logger.info("saved verify code to DB");
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS()).build()
        );
    }

    @Override
    public ResponseEntity<?> verifyCode(VerifyCodeDto verifyCodeDto) throws Exception {
        if (!validateVerifyCodeInformation(verifyCodeDto)) {
            logger.error("Parameter empty!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }

        User user = userRepository.findByEmail(verifyCodeDto.getEmail()).get();
        VerificationCode verifyCode = findVerificationCodeByUserId(user.getId());

        if (verifyCode == null ||
                !passwordEncoder.matches(verifyCodeDto.getVerifyCode(), verifyCode.getVerifyCode())) {
            logger.error("Verified code is invalid!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_INVALID())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_INVALID()).build()
            );
        }

        if (verifyCode.getExpiredTime().before(new Date())) {
            logger.error("Verified code is expired!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_TIME_OUT())
                            .message(commonProperties.getMESSAGE_PARAM_TIME_OUT()).build()
            );
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(new VerifyCodeResponse(verifyCode.getResetCode()))
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordDto resetPass) throws Exception {
        if (!validateResetPasswordInformation(resetPass)) {
            logger.error("Parameter invalid!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }

        User user = userRepository.findByEmail(resetPass.getEmail()).get();
        VerificationCode verifyCode = findVerificationCodeByUserId(user.getId());

        if (verifyCode == null ||
                !resetPass.getResetCode().equals(verifyCode.getResetCode())) {
            logger.error("Reset code is invalid!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_INVALID())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_INVALID()).build()
            );
        }

        String newPassword = passwordEncoder.encode(resetPass.getNewPassword());
        user.setPassword(newPassword);
        userRepository.save(user);

        verificationList.removeAll(
                verificationList.stream()
                        .filter(code -> code.getUser().getId() == user.getId())
                        .collect(Collectors.toList())
        );
        logger.info("Updated password!");

        TokenResponseInfo tokenResponseInfo = generateTokenResponseInfo(resetPass.getEmail(), resetPass.getNewPassword());

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(tokenResponseInfo).build()
        );
    }

    private boolean validateRegisterInformation(UserRegisterDto user) {
        return !user.getEmail().trim().isEmpty() &&
                !user.getPassword().trim().isEmpty() &&
                !user.getFullName().trim().isEmpty() &&
                !user.getDob().trim().isEmpty() &&
                !user.getPhoneNumber().trim().isEmpty();
    }

    private boolean validateChangePasswordInformation(UserChangePasswordDto user) {
        return !user.getEmail().trim().isEmpty() &&
                !user.getOldPassword().trim().isEmpty() &&
                !user.getNewPassword().trim().isEmpty();
    }

    private boolean validateVerifyCodeInformation(VerifyCodeDto user) {
        return !user.getEmail().trim().isEmpty() &&
                !user.getVerifyCode().trim().isEmpty();
    }

    private boolean validateResetPasswordInformation(ResetPasswordDto user) {
        return !user.getEmail().trim().isEmpty() &&
                !user.getResetCode().trim().isEmpty() &&
                !user.getNewPassword().trim().isEmpty();
    }

    private String generateRandomIntegerCode(int codeSize) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < codeSize; i++) {
            code.append(random.nextInt(9));
        }
        return code.toString();
    }

    private TokenResponseInfo generateTokenResponseInfo(String email, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email, password
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());



        return TokenResponseInfo.builder()
                .jwtToken(jwt)
                .user(new TokenResponseInfo().userResponse(userDetails,roles)).build();
    }

    private VerificationCode findVerificationCodeByUserId(Long userId) {
        ArrayList<VerificationCode> reverseList = verificationList;
        Collections.reverse(reverseList);

        return reverseList.stream()
                .filter(code -> code.getUser().getId() == userId)
                .findFirst().get();
    }


}
