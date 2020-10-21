package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.impl.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/meta_data")
public class MetadataController {

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveController.class);

    private CommonProperties commonProperties;

    private DepartmentServiceImpl departmentService;

    private ProjectServiceImpl projectService;

    private ProjectPositionServiceImpl positionService;

    private CycleServiceImpl cycleService;

    private EvaluationCriteriaServiceImpl criteriaService;

    @GetMapping(path = "/departments")
    public ResponseEntity<?> getListMetaDataDepartment(){
        try {
            return departmentService.getListMetaDataDepartment();
        } catch (Exception e) {
            logger.error("get meta data departments failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/projects")
    public ResponseEntity<?> getListMetaDataProject(){
        try {
            return projectService.getListMetaDataProject();
        } catch (Exception e) {
            logger.error("get meta data projects failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/positions")
    public ResponseEntity<?> getListMetaDataPosition(){
        try {
            return positionService.getListMetaDataPosition();
        } catch (Exception e) {
            logger.error("get meta data position failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/cycles")
    public ResponseEntity<?> getListMetaDataCycle(){
        try {
            return cycleService.getListMetaDataCycle();
        } catch (Exception e) {
            logger.error("get meta data cycle failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/evaluationCriteria")
    public ResponseEntity<?> getListMetaDataEvaluationCriteria(){
        try {
            return criteriaService.getListMetaDataEvaluation();
        } catch (Exception e) {
            logger.error("get meta data evaluation criteria failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}
