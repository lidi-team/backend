package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.UserChangePasswordDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @GetMapping("me")
    public ResponseEntity<?> getUserInformation(@RequestHeader(value = "Authorization") String jwtToken) {
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

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody UserChangePasswordDto userPassDto,
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

    @GetMapping("/all")
    public ResponseEntity<?> getAllUser(@RequestHeader(value = "Authorization") String jwtToken) {
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

    @GetMapping("")
    public ResponseEntity<?> getUserById(@RequestParam(name = "id") long id,
                                         @RequestHeader(value = "Authorization") String jwtToken){
        logger.info("Get info id: "+id);
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

    @GetMapping("/admin")
    public ResponseEntity<?> getNumberStaff(@RequestHeader(value = "Authorization") String jwtToken) {
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
}
