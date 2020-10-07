package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.KeyResultService;
import capstone.backend.api.service.impl.KeyResultServiceImpl;
import capstone.backend.api.service.impl.ObjectiveServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/v1")
public class ObjectiveController {

    private ObjectiveServiceImpl objectiveService;

    private KeyResultServiceImpl keyResultService;

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveController.class);

    private CommonProperties commonProperties;

    @PostMapping(path = "/objectives")
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

    @DeleteMapping(path = "/objectives/{id}")
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

    @GetMapping(path = "/objectives/list_okrs")
    public ResponseEntity<?> getlistObjective(@RequestParam(value = "cycleId") long cycleId){
        try {
            return objectiveService.getAllObjective(cycleId);
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

    @GetMapping(path = "/objectives/detail/{id}")
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

    @GetMapping(path = "/objectives/list_okrs/{id}")
    public ResponseEntity<?> getObjectivesByUserId(@PathVariable(value = "id") long id){
        try {
            return objectiveService.getObjectiveByUserId(id);
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

    @GetMapping(path = "/objectives/search")
    public ResponseEntity<?> searchByCycleIdandUserId(@RequestParam(value = "cycleId") long cycleId,
                                                      @RequestParam(value = "userId") long userId){
        try {
            return objectiveService.searchByCycleIdAndUserId(cycleId,userId);
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

    @GetMapping(path = "/objectives/view_list")
    public ResponseEntity<?> viewListObjectiveByCycleId(@RequestParam(value = "cycleId") long cycleId){
        try {
            return objectiveService.viewListObjectiveByCycleId(cycleId);
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

    @DeleteMapping(path = "/key_results/{id}")
    public ResponseEntity<?> deleteKeyResult(@PathVariable(value = "id") long id) {
        try {
            return keyResultService.deleteKeyResultById(id);
        } catch (Exception e) {
            logger.error("delete key result failed : " + id);
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}
