package capstone.backend.api.service;

import capstone.backend.api.dto.UserChangePasswordDto;
import capstone.backend.api.entity.ApiResponse.User.UserInforResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    ResponseEntity<?> getUserInformation(String jwtToken) throws Exception;

    ResponseEntity<?> changePassword(UserChangePasswordDto userChangePasswordDto, String jwtToken) throws Exception;

    ResponseEntity<?> saveAvatarLink(String url, String token) throws Exception;
    ResponseEntity<?> getAllUsers(String jwtToken) throws Exception;

    ResponseEntity<?> getUserInformationById(long id, String jwtToken) throws Exception;

    ResponseEntity<?> getNumberStaff(String jwtToken) throws Exception;

    ResponseEntity<?> getAllUsers(int page, int size, String sort, String jwtToken) throws Exception;

    ResponseEntity<?> getStaffPaging(int page, int size, String sort, String jwtToken) throws Exception;

    ResponseEntity<?> putUserInformationById(UserInforResponse userInfo, String jwtToken) throws Exception;

    ResponseEntity<?> isActiveUserById(long id, boolean isActive, String jwtToken) throws Exception;

    ResponseEntity<?> searchByName(String name, int page, int size, String sort, String jwtToken) throws Exception;
}
