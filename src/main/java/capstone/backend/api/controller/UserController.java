package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.UserChangePasswordDto;
import capstone.backend.api.dto.UserRegisterDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.User.UserInforResponse;
import capstone.backend.api.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping(value = "/api/user")
@RestController
@AllArgsConstructor
public class UserController {
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private CommonProperties commonProperties;

    @ApiOperation(value = "Thông tin tài khoản đăng nhập")
    @GetMapping("me")
    public ResponseEntity<?> getUserInformation(
            @ApiParam(value = "", required = true)
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Info me:");
        try {
            return userService.getUserInformation(jwtToken);
        } catch (Exception e) {
            logger.error("Get user information");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Thay đổi mật khẩu của tài khoản hiện tại")
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @ApiParam(value = "Thông tin password mới", required = true)
            @Valid @RequestBody UserChangePasswordDto userPassDto,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Change password:");
        try {
            return userService.changePassword(userPassDto, jwtToken);
        } catch (Exception e) {
            logger.error("change password failed for user: ");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "lưu avatar link của tài khoản")
    @PutMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@Valid @RequestParam(name = "avatarUrl") String url,
                                          @RequestHeader(value = "Authorization") String token) {
        try {
            return userService.saveAvatarLink(url, token);
        } catch (Exception e) {
            logger.error("upload image failed!");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Danh sách toàn bộ user, không phân trang")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUser(
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Find All Users:");
        try {
            return userService.getAllUsers(jwtToken);
        } catch (Exception e) {
            logger.error("Get all user information");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Danh sách toàn bộ user có phân trang")
    @GetMapping("/allPaging")
    public ResponseEntity<?> getAllUserPaging(
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "paging") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "size") int size,
            @ApiParam(value = "Kết quả trả về sắp xếp theo", required = true) @RequestParam(name = "sortWith") String sort,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Get user in paging: " + page);
        try {
            return userService.getAllUsers(page, size, sort, jwtToken);
        } catch (Exception e) {
            logger.error("Get user information");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message("Ngu Controller").build()
            );
        }
    }

    @ApiOperation(value = "Thông tin tài khoản theo ID")
    @GetMapping("")
    public ResponseEntity<?> getUserById(
            @ApiParam(value = "ID của tài khoản cần lấy id", required = true)
            @RequestParam(name = "id") long id,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Get info id: " + id);
        try {
            return userService.getUserInformationById(id, jwtToken);
        } catch (Exception e) {
            logger.error("Get user information");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message("Ngu Controller").build()
            );
        }
    }

    @ApiOperation(value = "số lượng pm và staff trên hệ thống")
    @GetMapping("/admin")
    public ResponseEntity<?> getNumberStaff(
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Get number PM & Staff");

        try {
            return userService.getNumberStaff(jwtToken);
        } catch (Exception e) {
            logger.error("Get all user information");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Danh sách Staff, có phân trang")
    @GetMapping("/listStaffPaging")
    public ResponseEntity<?> getStaffPaging(
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "paging") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "size") int size,
            @ApiParam(value = "Kết quả trả về sắp xếp theo", required = true) @RequestParam(name = "sortWith") String sort,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Get list staff in paging: " + page);
        try {
            return userService.getStaffPaging(page, size, sort, jwtToken);
        } catch (Exception e) {
            logger.error("Get user information");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message("Ngu Controller").build()
            );
        }
    }

    @ApiOperation(value = "cập nhật thông tin tài khoản Staff")
    @PutMapping("")
    public ResponseEntity<?> updateInfoStaff(
            @ApiParam(value = "", required = true)
            @Valid @RequestBody UserInforResponse userInfo,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Update info id: " + userInfo.getId());
        try {
            return userService.putUserInformationById(userInfo, jwtToken);
        } catch (Exception e) {
            logger.error("Get user information");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message("Ngu Controller").build()
            );
        }
    }

    @ApiOperation(value = "Kích hoạt/ Huỷ kích hoạt tài khoản theo ID")
    @GetMapping("isActive")
    public ResponseEntity<?> activeStaff(
            @ApiParam(value = "ID của tài khoản cần kích hoạt/ huỷ", required = true) @RequestParam(name = "id") long id,
            @ApiParam(value = "true: kích hoạt, false: huỷ kích hoạt", required = true) @RequestParam(name = "isActive") boolean isActive,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Active/ De-active user id: " + id);
        try {
            return userService.isActiveUserById(id, isActive, jwtToken);
        } catch (Exception e) {
            logger.error("Get user information");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message("Ngu Controller").build()
            );
        }
    }

    @ApiOperation(value = "Tìm nhân viên theo tên, có phân trang")
    @GetMapping("search")
    public ResponseEntity<?> searchByName(
            @ApiParam(value = "Tên nhân vật cần tìm", required = true) @RequestParam(name = "name") String name,
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "paging") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "size") int size,
            @ApiParam(value = "Kết quả trả về sắp xếp theo", required = true) @RequestParam(name = "sortWith") String sort,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Search user by name: " + name);
        try {
            return userService.searchByName(name, page, size, sort, jwtToken);
        } catch (Exception e) {
            logger.error("Get user information");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message("Ngu Controller").build()
            );
        }
    }

    @ApiOperation(value = "thêm mới danh sách staff vào hệ thống")
    @PutMapping(value = "/add-staff")
    public ResponseEntity<?> addListStaff(
            @ApiParam(value = "danh sach staff",required = true)
            @RequestBody UserRegisterDto users) {

        try {
            return userService.addListUsers(users);
        } catch (Exception e) {
            logger.error("Get all user information");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}
