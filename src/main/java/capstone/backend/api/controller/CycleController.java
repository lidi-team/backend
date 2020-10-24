package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.Cycle;
import capstone.backend.api.service.CycleService;
import capstone.backend.api.service.impl.CycleServiceImpl;
import capstone.backend.api.service.impl.ObjectiveServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/cycle")
public class CycleController {
    private CommonProperties commonProperties;

    private CycleServiceImpl cycleService;

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveServiceImpl.class);


    @ApiOperation(value = "")
    @GetMapping(path = "/current")
    public ResponseEntity<?> getCurrentCycle(
            @ApiParam(value = "", required = true)
            @PathVariable(value = "id") long id) {
        try {
            return null;
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

    @ApiOperation(value = "")
    @GetMapping(path = "/all")
    public ResponseEntity<?> viewListCycles(
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "paging") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "size") int size,
            @ApiParam(value = "Kết quả trả về sắp xếp theo", required = true) @RequestParam(name = "sortWith") String sort,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return cycleService.getAllCycles(page, size, sort, jwtToken);
        } catch (Exception e) {
            logger.error("get list cycles failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "")
    @PostMapping(path = "create")
    public ResponseEntity<?> createCycle(
            @ApiParam(value = "", required = true) @Valid @RequestBody Cycle cycle,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Create cycle: " + cycle.getFromDate());
        try {
            return cycleService.createCycle(cycle, jwtToken);
        } catch (Exception e) {
            logger.error("Create cycle failed!");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "")
    @PutMapping(path = "update")
    public ResponseEntity<?> updateCycle(
            @ApiParam(value = "", required = true) @Valid @RequestBody Cycle cycle,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Update cycle: " + cycle.getName());
        try {
            return cycleService.updateCycle(cycle, jwtToken);
        } catch (Exception e) {
            logger.error("Update cycle failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "")
    @DeleteMapping(path = "delete")
    public ResponseEntity<?> deleteCycle(
            @ApiParam(value = "", required = true) @RequestParam(name = "id") long id,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Delete cycle: " + id);
        try {
            return cycleService.deleteCycle(id, jwtToken);
        } catch (Exception e) {
            logger.error("Delete cycle failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "")
    @GetMapping(path = "search")
    public ResponseEntity<?> searchByDate(
            @ApiParam(value = "", required = true) @RequestParam(name = "date") String date,
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "paging") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "size") int size,
            @ApiParam(value = "Kết quả trả về sắp xếp theo", required = true) @RequestParam(name = "sortWith") String sort,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Search cycle: " + date);
        try {
            Date dateSreach = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            return cycleService.searchCycle(dateSreach, page, size, sort, jwtToken);
        } catch (Exception e) {
            logger.error("Search cycle failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}