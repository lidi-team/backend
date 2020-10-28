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

    private final ObjectiveServiceImpl objectiveService;

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveController.class);

    private final CommonProperties commonProperties;

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

    @GetMapping(path = "/child-objectives")
    public ResponseEntity<?> getListChildObjectiveByObjectiveId(@RequestParam(name = "objectiveId") long objectiveId,
                                                                @RequestParam(name = "cycleId") long cycleId){
        try {
            return objectiveService.getListChildObjectiveByObjectiveId(objectiveId,cycleId);
        } catch (Exception e) {
            logger.error("get list child objectives failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/list-okr/{userId}")
    public ResponseEntity<?> getListObjectiveTitleByUserId(@PathVariable(name = "userId") long userId){
        try {
            return objectiveService.getListObjectiveTitleByUserId(userId);
        } catch (Exception e) {
            logger.error("get list objective titles failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/parent-okr/{id}")
    public ResponseEntity<?> getParentObjectiveTitleByObjectiveId(@PathVariable(name = "id") long id,
                                                                  @RequestHeader(name = "Authorization") String token){
        try {
            return objectiveService.getParentObjectiveTitleByObjectiveId(id,token);
        } catch (Exception e) {
            logger.error("get parent objective titles failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/parent-key_result/{id}")
    public ResponseEntity<?> getListParentKeyResultByObjectiveId(@PathVariable(value = "id") long id){
        try {
            return objectiveService.getParentKeyResultTitleByObjectiveId(id);
        } catch (Exception e) {
            logger.error("get list child objectives failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/align-objective/{id}")
    public ResponseEntity<?> getListAlignByObjectiveId(@PathVariable(value = "id") long id){
        try {
            return objectiveService.getListAlignByObjectiveId(id);
        } catch (Exception e) {
            logger.error("get list align objectives failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

}
