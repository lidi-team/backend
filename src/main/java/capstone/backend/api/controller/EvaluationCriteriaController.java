package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.DepartmentDto;
import capstone.backend.api.dto.EvaluationCriteriaDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.EvaluationCriteriaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/evaluations")
@AllArgsConstructor
public class EvaluationCriteriaController {

    private EvaluationCriteriaService evaluationCriteriaService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private CommonProperties commonProperties;

    @ApiOperation(value = "lấy toàn bộ Evaluation, có phân trang")
    @GetMapping
    public ResponseEntity<?> viewListEvaluation(
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "page") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "limit") int limit,
            @ApiParam(value = "Noi dung", required = false) @RequestParam(name = "text", required = false) String text,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return evaluationCriteriaService.getAllEvaluation(page, limit, text, jwtToken);
        } catch (Exception e) {
            logger.error("get list Evaluation failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "tạo Evaluation mới")
    @PostMapping()
    public ResponseEntity<?> createEvaluation(
            @ApiParam(value = "", required = true)
            @Valid @RequestBody EvaluationCriteriaDto evaluationCriteriaDto,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return evaluationCriteriaService.createEvaluation(evaluationCriteriaDto, jwtToken);
        } catch (Exception e) {
            logger.error("Create Evaluation failed!");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "lấy thông tin Evaluation theo id")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getEvaluation(
            @ApiParam(required = true)
            @PathVariable(value = "id") long id,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return evaluationCriteriaService.getEvaluation(id, jwtToken);
        } catch (Exception e) {
            logger.error("get Department failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "cập nhật thông tin Evaluation")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateEvaluation(
            @ApiParam(required = true) @PathVariable(value = "id") long id,
            @ApiParam(required = true) @Valid @RequestBody EvaluationCriteriaDto evaluationCriteriaDto,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Update Evaluation: " + evaluationCriteriaDto.getContent());
        try {
            return evaluationCriteriaService.updateEvaluation(id, evaluationCriteriaDto, jwtToken);
        } catch (Exception e) {
            logger.error("Update Evaluation failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "xoá một Evaluation theo id")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteEvaluation(
            @ApiParam(required = true) @PathVariable(name = "id") long id,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Delete Evaluation: " + id);
        try {
            return evaluationCriteriaService.deleteEvaluation(id, jwtToken);
        } catch (Exception e) {
            logger.error("Delete Evaluation failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}
