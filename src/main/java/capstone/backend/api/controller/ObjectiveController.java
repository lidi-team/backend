package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.impl.ObjectiveServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/objective")
public class ObjectiveController {

    private ObjectiveServiceImpl objectiveService;

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveController.class);

    private CommonProperties commonProperties;

    @PostMapping(path = "/add")
    public ResponseEntity<?> addObjective(@Valid @RequestBody ObjectvieDto objectvieDto) {
        try {
            return objectiveService.addObjective(objectvieDto);
        } catch (Exception e) {
            logger.error("add objective failed : " + objectvieDto.getTitle());
            logger.error(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteObjective(@PathVariable(value = "id") long id) {
        try {
            return objectiveService.deleteObjective(id);
        } catch (Exception e) {
            logger.error("delete objective failed : " + id);
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/get/all")
    public ResponseEntity<?> getlistObjective(){
        try {
            return objectiveService.getAllObjective();
        } catch (Exception e) {
            logger.error("get list objective failed : ");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/get/{id}")
    public ResponseEntity<?> getObjectiveByObjectiveId(@PathVariable(value = "id") long id){
        try {
            return objectiveService.getObjectiveByObjectiveId(id);
        } catch (Exception e) {
            logger.error("get objective failed : ");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}
