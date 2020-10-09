package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RequestMapping(value = "/api/user")
@RestController
@AllArgsConstructor
public class UserController {
    private UserServiceImpl userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private CommonProperties commonProperties;

    /**
     * get basic information of user
     * @param email
     * @return user(email , password ,dob ,firstName ,lastName,phoneNumber ,gender ,avatarImage ,roles)
     * @throws Exception
     */
    @GetMapping(value = "basic-information/{email}")
    public ResponseEntity<?> getStudentAccount(@NotNull @PathVariable(value = "email")String email) throws Exception{
        try{
            return userService.getUserByEmail(email);
        }catch (Exception e){
            logger.error("get basic-information of user failed!");
            logger.error(e.getMessage());
            throw  new Exception("cannot get information of user with email: "+email+"!");
        }
    }

    @GetMapping("me")
    public ResponseEntity<?> getUserInformation(@RequestHeader(value = "Authorization") String jwtToken) {
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

}
