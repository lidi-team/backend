package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.UserChangePasswordDto;
import capstone.backend.api.dto.UserLoginDto;
import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.Role;
import capstone.backend.api.entity.User;
import capstone.backend.api.entity.security.TokenResponseInfo;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.AuthenticationService;
import capstone.backend.api.service.impl.security.UserDetailsImpl;
import capstone.backend.api.utils.DateUtils;
import capstone.backend.api.utils.RoleUtils;
import capstone.backend.api.utils.security.JwtUtils;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.services.gmail.Gmail;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Random;
import java.util.Set;
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

    @Override
    public ResponseEntity<?> authenticate(UserLoginDto userLoginDto) throws AuthenticationException {
        if(StringUtils.isEmpty(userLoginDto.getEmail().trim())
                || StringUtils.isEmpty(userLoginDto.getPassword().trim())){
            logger.error("Email or password is empty!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder().code(405).message("Email or password is empty!").build()
            );
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getEmail(), userLoginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toSet());
        logger.info("authenticate Ok!");
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(200).message("Authentication successful!")
                        .data(new TokenResponseInfo(jwt, userDetails.getUsername(), roles)).build()
        );

    }

    @Override
    public ResponseEntity<?> register(UserRegisterDto userRegisterDto) throws AuthenticationException {
        if(!validateRegisterInformation(userRegisterDto).isEmpty()){
            logger.error(validateRegisterInformation(userRegisterDto));
            return ResponseEntity.badRequest().body(
                   ApiResponse.builder().code(405).message(validateRegisterInformation(userRegisterDto)).build()
            );
        }
        if (userRepository.existsUserByEmail(userRegisterDto.getEmail())) {
            logger.error("email is already in used!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder().code(405).message("Email is already in used!").build()
            );
        }
        String passwordEncode = passwordEncoder.encode(userRegisterDto.getPassword());
        String dobStr = userRegisterDto.getDob();
        Date dob;
        try {
            dob = DateUtils.stringToDate(dobStr,DateUtils.PATTERN_ddMMyyyy);
        } catch (ParseException e) {
            logger.error("parse dob in register of user failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder().code(405).message("Parse failed! Valid format 'dd/MM/yyyy'").build()
            );
        }
        Set<String> strRoles = userRegisterDto.getRoles();
        Set<Role> roles = roleUtils.getUserRoles(strRoles);

        userRepository.save(
                User.builder()
                        .email(userRegisterDto.getEmail())
                        .password(passwordEncode).dob(dob)
                        .fullName(userRegisterDto.getFullName())
                        .phoneNumber(userRegisterDto.getPhoneNumber())
                        .gender(userRegisterDto.getGender())
                        .roles(roles).build());
        logger.info("user " + userRegisterDto.getEmail() + " register successfully!");
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(200).message("Register user successfully!").build()
        );
    }

    @Override
    public ResponseEntity<?> changePassword(UserChangePasswordDto userPassDto) throws AuthenticationException {
        if(!validateChangePasswordInformation(userPassDto).isEmpty()){
            logger.error(validateChangePasswordInformation(userPassDto));
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder().code(405).message(validateChangePasswordInformation(userPassDto)).build()
            );
        }

        String oldPassword = passwordEncoder.encode(userPassDto.getOldPassword());
        if(!userRepository.existsUserByEmailAndPassword(userPassDto.getEmail(),oldPassword)){
            logger.error("Old password is incorrect!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder().code(405).message("Old password is incorrect!").build()
            );
        }

        String newPassword = passwordEncoder.encode(userPassDto.getNewPassword());
        userRepository.updateUserPassword(newPassword,userPassDto.getEmail());
        logger.info("update successful!");
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(200).message("Update password successful!").build()
        );
    }

    @Override
    public ResponseEntity<?> getVerifyCode(String email) throws AuthenticationException, MessagingException, IOException {
        if(StringUtils.isEmpty(email.trim())){
            logger.error("Email is empty!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder().code(405).message("Email is empty!").build()
            );
        }

        String randomCode = generateRandomIntegerCode(commonProperties.getCodeSize());
        String fromEmail = commonProperties.getLidiEmail();
        String subject = "LidiEdu get code to change password";
        String bodyText = "Your verify code is: "+ randomCode;
        MimeMessage message = mailService.createEmail(email,fromEmail,subject,bodyText);


       // mailService.sendMessage(null,email,message);
        return null;
    }

    private String validateRegisterInformation(UserRegisterDto user){
        if(user.getEmail().trim().isEmpty()){
            return "Email is empty!";
        }
        if(user.getPassword().trim().isEmpty()){
            return "Password is empty!";
        }
        if(user.getFullName().trim().isEmpty()){
            return "Full name is empty!";
        }
        if(user.getDob().trim().isEmpty()){
            return "Dob is empty!";
        }
        if(user.getPhoneNumber().trim().isEmpty()){
            return "Phone number is empty!";
        }
        if(user.getGender() >1 || user.getGender() < -1){
            return "Gender is in [-1,0,1] only!";
        }
        return "";
    }

    private String validateChangePasswordInformation(UserChangePasswordDto user){
        if(user.getEmail().trim().isEmpty()){
            return "Email is empty!";
        }
        if(user.getOldPassword().trim().isEmpty()){
            return "Old password is empty!";
        }
        if(user.getNewPassword().trim().isEmpty()){
            return "New password is empty!";
        }
        return "";
    }

    private String generateRandomIntegerCode(int codeSize){
        String code = "";
        Random random = new Random();
        for (int i = 0; i < codeSize; i++) {
            code += random.nextInt(9);
        }
        return code;
    }
}
