package capstone.backend.api.service;

import capstone.backend.api.dto.UpdateUserInfoDto;
import capstone.backend.api.dto.UserChangePasswordDto;
import capstone.backend.api.dto.UserRegister;
import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.entity.ApiResponse.User.UserInforResponse;
import capstone.backend.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    ResponseEntity<?> getAllUsers(String name, int page, int size, String sort, String jwtToken) throws Exception;

    ResponseEntity<?> getStaffPaging(String name, int page, int size, String sort, String jwtToken) throws Exception;

    ResponseEntity<?> putUserInformationById(UpdateUserInfoDto userInfo,long id) throws Exception;

    ResponseEntity<?> isActiveUserById(long id, boolean isActive, String jwtToken) throws Exception;

    ResponseEntity<?> searchByName(String name, int page, int size, String sort, String jwtToken) throws Exception;

    ResponseEntity<?> addListUsers(UserRegisterDto userRegisterDto) throws Exception;
}
