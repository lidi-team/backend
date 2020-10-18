package capstone.backend.api.service;

import capstone.backend.api.dto.UserChangePasswordDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    ResponseEntity<?> getUserInformation(String jwtToken) throws Exception;

    ResponseEntity<?> changePassword(UserChangePasswordDto userChangePasswordDto, String jwtToken) throws Exception;

}
