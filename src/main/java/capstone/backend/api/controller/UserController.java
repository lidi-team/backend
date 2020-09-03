package capstone.backend.api.controller;

import capstone.backend.api.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RequestMapping(value = "/api/user")
@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

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
}
