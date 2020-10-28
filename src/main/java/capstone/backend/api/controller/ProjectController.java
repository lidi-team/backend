package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.impl.ObjectiveServiceImpl;
import capstone.backend.api.service.impl.ProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/project")
public class ProjectController {

    private final CommonProperties commonProperties;

    private final ProjectServiceImpl projectService;

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveServiceImpl.class);

    @GetMapping(path = "/all")
    public ResponseEntity<?> getAllProject(){
        try {
            return projectService.getAllProjects();
        } catch (Exception e) {
            logger.error("get list project failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/available")
    public ResponseEntity<?> getAllCurrentProjectOfUser(@RequestHeader(name = "Authorization") String token,
                                                        @RequestParam(name = "type") int type){
        try {
            return projectService.getAllAvailableProjectOfUser(token,type);
        } catch (Exception e) {
            logger.error("get list project available of a user failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}
