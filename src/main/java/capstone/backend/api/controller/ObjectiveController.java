package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.impl.ObjectiveServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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


    @ApiOperation(value = "Thêm Objective")
    @PostMapping(path = "/add")
    public ResponseEntity<?> addObjective(
            @ApiParam(value = "Thông tin của một Objective", required = true)
            @Valid @RequestBody ObjectvieDto objectvieDto,
            @ApiParam(value = "Thông tin của user", required = true)
            @RequestHeader(value = "Authorization") String token) {
        try {
            return objectiveService.addObjective(objectvieDto,token);
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


    @ApiOperation(value = "Xoá một Objective theo id")
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteObjective(
            @ApiParam(value = "ID của objective cần xoá", required = true)
            @PathVariable(value = "id") long id) {
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


    @ApiOperation(value = "Danh sách Objective con theo ID của objective cha")
    @GetMapping(path = "/child-objectives")
    public ResponseEntity<?> getListChildObjectiveByObjectiveId(
            @ApiParam(value = "ID của Objective cha", required = true)
            @RequestParam(name = "objectiveId") long objectiveId,
            @ApiParam(value = "ID của Cycle cần lấy danh sách Objective con")
            @RequestParam(name = "cycleId") long cycleId) {
        try {
            return objectiveService.getListChildObjectiveByObjectiveId(objectiveId, cycleId);
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

    @ApiOperation(value = "Danh sách Objective theo User ID")
    @GetMapping(path = "/list-okr/{userId}")
    public ResponseEntity<?> getListObjectiveTitleByUserId(
            @ApiParam(value = "User ID cần lấy danh sách Objective", required = true)
            @PathVariable(name = "userId") long userId) {
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

    @ApiOperation(value = "Danh sách Objectives cha của objective hiện tại")
    @GetMapping(path = "/parent-okr/{id}")
    public ResponseEntity<?> getParentObjectiveTitleByObjectiveId(@PathVariable(name = "id") long id,
                                                                  @RequestHeader(name = "Authorization") String token) {
        try {
            return objectiveService.getParentObjectiveTitleByObjectiveId(id, token);
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

    @ApiOperation(value = "Danh sách key result cha của objective hiện tại")
    @GetMapping(path = "/parent-key_result/{id}")
    public ResponseEntity<?> getListParentKeyResultByObjectiveId(@PathVariable(value = "id") long id) {
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

    @ApiOperation(value = "Danh sách objective có thể liên kết theo project")
    @GetMapping(path = "/align-objective")
    public ResponseEntity<?> getListAlignByObjectiveId(
            @ApiParam(value = "Id của project",required = true)
            @RequestParam(value = "projectId") long projectId,
            @ApiParam(value = "Id của project",required = true)
            @RequestParam(value = "cycleId") long cycleId) {
        try {
            return objectiveService.getListAlignByProjectIdAndCycleId(projectId,cycleId);
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

    @ApiOperation(value = "Danh sách key result cha của objective hiện tại")
    @GetMapping(path = "/key_result/{id}")
    public ResponseEntity<?> getListKeyResultByObjectiveId(@PathVariable(value = "id") long id) {
        try {
            return objectiveService.getKeyResultTitleByObjectiveId(id);
        } catch (Exception e) {
            logger.error("get list key result failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Tất cả objective và project hiện tại user đang tham gia")
    @GetMapping(path = "/project-list")
    public ResponseEntity<?> getAllObjectiveAndProjectOfUser(
            @ApiParam(value = "token",required = true)
            @RequestHeader(name = "Authorization") String token,
            @ApiParam(value = "cycle id cua cycle hien tai")
            @RequestParam(name = "cycleId") long cycleId){
        try {
            return objectiveService.getAllObjectiveAndProjectOfUser(token,cycleId);
        } catch (Exception e) {
            logger.error("get list objective and project of a user failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Thông tin chi tiết của objective theo id")
    @GetMapping(path = "/detail/{id}")
    public ResponseEntity<?> getDetailObjectiveById(
            @ApiParam(value = "id cua Objective hien tai")
            @PathVariable(value = "id") long id){
        try {
            return objectiveService.getDetailObjectiveById(id);
        } catch (Exception e) {
            logger.error("get detail objective failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

}
