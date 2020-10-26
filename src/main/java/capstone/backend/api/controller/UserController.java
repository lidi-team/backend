package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.UserChangePasswordDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.User.UserInforResponse;
import capstone.backend.api.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping(value = "/api/user")
@RestController
@AllArgsConstructor
public class UserController {
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private CommonProperties commonProperties;

    @ApiOperation(value = "Th�ng tin t�i kho?n dang dang nh?p")
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

    @ApiOperation(value = "Thay d?i password t�i kho?n dang dang nh?p")
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @ApiParam(value = "Th�ng tin t�i kho?n c?n d?i password", required = true)
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

    @PutMapping("/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@Valid @RequestParam(name = "avatarUrl") String url,
                                            @RequestHeader(value = "Authorization") String token) {
        try {
            return userService.saveAvatarLink(url,token);
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

    @ApiOperation(value = "Danh s�ch to�n b? user, kh�ng ph�n trang")
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

    @ApiOperation(value = "Danh s�ch to�n b? user, c� ph�n trang")
    @GetMapping("/allPaging")
    public ResponseEntity<?> getAllUserPaging(
            @ApiParam(value = "S? trang c?n truy v?n, trang d?u ti�n l� 0", required = true) @RequestParam(name = "paging") int page,
            @ApiParam(value = "S? lu?ng k?t qu? tr�n m?i trang, s? nguy�n", required = true) @RequestParam(name = "size") int size,
            @ApiParam(value = "K?t qu? tr? v? s?p x?p theo", required = true) @RequestParam(name = "sortWith") String sort,
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

    @ApiOperation(value = "Th�ng tin t�i kho?n theo ID")
    @GetMapping("")
    public ResponseEntity<?> getUserById(
            @ApiParam(value = "ID c?a t�i kho?n c?n l?y th�ng tin", required = true)
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

    @ApiOperation(value = "S? lu?ng PM v� Staff tr�n h? th?ng")
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

    @ApiOperation(value = "Danh s�ch Staff, c� ph�n trang")
    @GetMapping("/listStaffPaging")
    public ResponseEntity<?> getStaffPaging(
            @ApiParam(value = "S? trang c?n truy v?n, trang d?u ti�n l� 0", required = true) @RequestParam(name = "paging") int page,
            @ApiParam(value = "S? lu?ng k?t qu? tr�n m?i trang, s? nguy�n", required = true) @RequestParam(name = "size") int size,
            @ApiParam(value = "K?t qu? tr? v? s?p x?p theo", required = true) @RequestParam(name = "sortWith") String sort,
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

    @ApiOperation(value = "C?p nh?t th�ng tin c?a t�i kho?n Staff")
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

    @ApiOperation(value = "K�ch ho?t/ Hu? k�ch ho?t t�i kho?n theo ID")
    @GetMapping("isActive")
    public ResponseEntity<?> activeStaff(
            @ApiParam(value = "ID c?a t�i kho?n c?n k�ch ho?t/ hu?", required = true) @RequestParam(name = "id") long id,
            @ApiParam(value = "true: k�ch ho?t, false: hu? k�ch ho?t", required = true) @RequestParam(name = "isActive") boolean isActive,
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

    @ApiOperation(value = "T�m nh�n vi�n theo t�n, c� ph�n trang")
    @GetMapping("search")
    public ResponseEntity<?> searchByName(
            @ApiParam(value = "T�n nh�n v?t c?n t�m", required = true) @RequestParam(name = "name") String name,
            @ApiParam(value = "S? trang c?n truy v?n, trang d?u ti�n l� 0", required = true) @RequestParam(name = "paging") int page,
            @ApiParam(value = "S? lu?ng k?t qu? tr�n m?i trang, s? nguy�n", required = true) @RequestParam(name = "size") int size,
            @ApiParam(value = "K?t qu? tr? v? s?p x?p theo", required = true) @RequestParam(name = "sortWith") String sort,
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
}
