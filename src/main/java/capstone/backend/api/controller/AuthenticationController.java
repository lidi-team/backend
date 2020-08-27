package capstone.backend.api.controller;

import capstone.backend.api.dto.UserLoginDto;
import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.exception.UnauthorizedException;
import capstone.backend.api.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private AuthenticationService authenticationService;
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@Valid @RequestBody UserLoginDto userLoginDto) throws UnauthorizedException {
        try{
            return authenticationService.authenticate(userLoginDto);
        }catch(AuthenticationException e){
            logger.error("Authenticate failed for user: " + userLoginDto.getUsername());
            logger.error(e.getMessage());
            throw new UnauthorizedException("Authenticate failed for user: " + userLoginDto.getUsername());
        }
    }
    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterDto userRegisterDto) throws UnauthorizedException{
        try{
            return authenticationService.register(userRegisterDto);
        }catch (AuthenticationException e){
            logger.error("Authenticate failed for user: " + userRegisterDto.getUsername());
            logger.error(e.getMessage());
            throw new UnauthorizedException("Authenticate failed for user: " + userRegisterDto.getUsername());
        }
    }
}
