package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.Cycle;
import capstone.backend.api.service.impl.CycleServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/cycle")
@Api(value = "Chu kì")
public class CycleController {
    private CommonProperties commonProperties;

    private CycleServiceImpl cycleService;
    private static final Logger logger = LoggerFactory.getLogger(CycleController.class);


    @ApiOperation(value = "lấy thông tin chu kì theo id")
    @GetMapping(path = "/current/{id}")
    public ResponseEntity<?> getCurrentCycle(
            @ApiParam(required = true)
            @PathVariable(value = "id") long id) {
        try {
            return cycleService.getCurrentCycle(id);
        } catch (Exception e) {
            logger.error("get current cycle failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "lấy tất cả cycle, có phân trang")
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

    @ApiOperation(value = "tạo cycle mới")
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

    @ApiOperation(value = "cập nhật thông tin cycle")
    @PutMapping(path = "update")
    public ResponseEntity<?> updateCycle(
            @ApiParam(required = true) @Valid @RequestBody Cycle cycle,
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

    @ApiOperation(value = "xoá một cycle theo id")
    @DeleteMapping(path = "delete")
    public ResponseEntity<?> deleteCycle(
            @ApiParam(required = true) @RequestParam(name = "id") long id,
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

    @ApiOperation(value = "tìm kiếm cycle, có sắp xếp")
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
