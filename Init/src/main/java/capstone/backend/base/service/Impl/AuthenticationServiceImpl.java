package capstone.backend.base.service.Impl;

import capstone.backend.base.DTO.UserLoginDto;
import capstone.backend.base.DTO.UserRegisterDto;
import capstone.backend.base.entity.Role;
import capstone.backend.base.entity.TokenResponseInfo;
import capstone.backend.base.entity.User;
import capstone.backend.base.repository.RoleRepository;
import capstone.backend.base.repository.UserRepository;
import capstone.backend.base.service.AuthenticationService;
import capstone.backend.base.service.Impl.security.UserDetailsImpl;
import capstone.backend.base.utils.JwtUtils;
import capstone.backend.base.utils.RoleConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    AuthenticationManager authenticationManager;

    UserRepository userRepository;

    PasswordEncoder passwordEncoder;

    JwtUtils jwtUtils;


    @Override
    public ResponseEntity<?> authenticate(UserLoginDto userLoginDto) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getUsername(), userLoginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toSet());
        logger.info("authenticate Ok!");
        return ResponseEntity.ok(
                new TokenResponseInfo(jwt,userDetails.getUsername(),roles)
        );

    }

    @Override
    public ResponseEntity<?> register(UserRegisterDto userRegisterDto) throws AuthenticationException {
        if(userRepository.existsUserByUsername(userRegisterDto.getUsername())){
            return ResponseEntity.badRequest()
                    .body("Username is already in used!");
        }
        String passwordEncode = passwordEncoder.encode(userRegisterDto.getPassword());
        Set<String> strRoles = userRegisterDto.getRoles();
        Set<Role> roles = new HashSet<>();

        if(strRoles == null){
            roles.add(new Role(RoleConstants.ROLE_USER));
        } else{
            strRoles.forEach(role->{
                switch (role){
                    case RoleConstants.ROLE_USER:
                    {
                        roles.add(new Role(RoleConstants.ROLE_USER));
                    }
                    case RoleConstants.ROLE_TEACHER:
                    {
                        roles.add(new Role(RoleConstants.ROLE_TEACHER));
                    }
                }
            });
        }
        User user = userRepository.saveAndFlush(
                new User(userRegisterDto.getUsername(),
                        passwordEncode,
                        userRegisterDto.getAge(),
                        userRegisterDto.getFullName(),
                        roles));
        logger.info("user "+ userRegisterDto.getUsername()+" register successfully!");
        return ResponseEntity.ok(user);
    }
}
