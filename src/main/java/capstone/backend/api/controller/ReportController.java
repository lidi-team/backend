package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.CheckinDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.impl.ReportServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/checkin")
public class ReportController {

    private CommonProperties commonProperties;

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    private ReportServiceImpl reportService;


    @GetMapping(path = "/list-history/{objectiveId}")
    public ResponseEntity<?> getCheckinHistoryByObjectiveId(@PathVariable(value = "objectiveId") long objectiveId){
        try {
            return reportService.getCheckinHistoryByObjectiveId(objectiveId);
        } catch (Exception e) {
            logger.error("get list checkin history failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addNewCheckin(@RequestBody() CheckinDto checkinDto){
        try {
            return reportService.addCheckin(checkinDto);
        } catch (Exception e) {
            logger.error("get list checkin history failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @GetMapping(path = "/list-objective")
    public ResponseEntity<?> getListObjectiveByCycleId(@RequestHeader(name = "Authorization") String token,
                                                       @RequestParam(name = "cycleId") long id){
        try {
            return reportService.getListObjectiveByCycleId(token,id);
        } catch (Exception e) {
            logger.error("get list objective checkin failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}
