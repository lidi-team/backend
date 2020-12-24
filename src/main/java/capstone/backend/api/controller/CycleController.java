package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.CreateCycleDto;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/cycle")
@Api(value = "Chu kì")
public class CycleController {
    private final CommonProperties commonProperties;

    private final CycleServiceImpl cycleService;
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
    @PreAuthorize("hasRole(commonProperties.getROLE_ADMIN())")
    @ApiOperation(value = "lấy tất cả cycle, có phân trang")
    @GetMapping()
    public ResponseEntity<?> viewListCycles(
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 1", required = true) @RequestParam(name = "page") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "limit") int limit,
            @ApiParam(value = "Noi dung", required = false) @RequestParam(name = "text", required = false) String text) {
        try {
            return cycleService.getAllCycles(page, limit, text);
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
    @PreAuthorize("hasRole(commonProperties.getROLE_ADMIN())")
    @ApiOperation(value = "tạo cycle mới")
    @PostMapping()
    public ResponseEntity<?> createCycle(
            @ApiParam(value = "", required = true)
            @Valid @RequestBody CreateCycleDto cycle) {
        try {
            return cycleService.createCycle(cycle);
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
    @PreAuthorize("hasRole(commonProperties.getROLE_ADMIN())")
    @ApiOperation(value = "cập nhật thông tin cycle")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateCycle(
            @ApiParam(required = true) @PathVariable(value = "id") long id,
            @ApiParam(required = true) @Valid @RequestBody CreateCycleDto cycle) {
        logger.info("Update cycle: " + cycle.getName());
        try {
            return cycleService.updateCycle(id,cycle);
        } catch (Exception e) {
            logger.error("Update cycle failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
    @PreAuthorize("hasRole(commonProperties.getROLE_ADMIN())")
    @ApiOperation(value = "xoá một cycle theo id")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteCycle(
            @ApiParam(required = true) @PathVariable(name = "id") long id,
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

    @ApiOperation(value = "tìm kiếm cycle theo id")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> findById(@PathVariable(value = "id")long id) {
        try {
            return cycleService.findById(id);
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
