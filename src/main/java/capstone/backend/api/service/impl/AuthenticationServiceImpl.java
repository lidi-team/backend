package capstone.backend.api.service.impl;

import capstone.backend.api.dto.UserLoginDto;
import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.entity.Role;
import capstone.backend.api.entity.security.TokenResponseInfo;
import capstone.backend.api.entity.User;
import capstone.backend.api.repository.RoleRepository;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.AuthenticationService;
import capstone.backend.api.service.impl.security.UserDetailsImpl;
import capstone.backend.api.utils.RoleConstants;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private RoleRepository roleRepository;

    private JwtUtils jwtUtils;


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
            Role role = roleRepository.findRoleByName(RoleConstants.ROLE_USER).get();
            roles.add(role);
        } else{
            strRoles.forEach(role->{
                switch (role){
                    case RoleConstants.ROLE_USER:
                    {
                        Role roleDb = roleRepository.findRoleByName(RoleConstants.ROLE_USER).get();
                        roles.add(roleDb);
                    }
                    case RoleConstants.ROLE_TEACHER:
                    {
                        Role roleDb = roleRepository.findRoleByName(RoleConstants.ROLE_TEACHER).get();
                        roles.add(roleDb);
                    }
                }
            });
        }
        userRepository.save(
                new User(userRegisterDto.getUsername(),
                        passwordEncode,
                        userRegisterDto.getAge(),
                        userRegisterDto.getFullName(),
                        roles));
        logger.info("user "+ userRegisterDto.getUsername()+" register successfully!");
        return ResponseEntity.ok("Save successfully!");
    }
}
