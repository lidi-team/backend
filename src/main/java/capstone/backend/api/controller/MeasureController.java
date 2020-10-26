package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.UnitOfKeyResult;
import capstone.backend.api.service.impl.ObjectiveServiceImpl;
import capstone.backend.api.service.impl.UnitOfKeyResultServiceImpl;
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
@RequestMapping(value = "/api/measure")
public class MeasureController {
    private CommonProperties commonProperties;
    private static final Logger logger = LoggerFactory.getLogger(MeasureController.class);
    private UnitOfKeyResultServiceImpl unitOfKeyResultService;

    @ApiOperation(value = "")
    @GetMapping(path = "/all")
    public ResponseEntity<?> viewListCycles(
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "paging") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "size") int size,
            @ApiParam(value = "Kết quả trả về sắp xếp theo", required = true) @RequestParam(name = "sortWith") String sort,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return unitOfKeyResultService.getAllMeasure(page, size, sort, jwtToken);
        } catch (Exception e) {
            logger.error("get list measure failed");
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
    public ResponseEntity<?> createMeasure(
            @ApiParam(value = "", required = true) @Valid @RequestBody UnitOfKeyResult unit,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Create measure: " + unit.getName());
        try {
            return unitOfKeyResultService.createMeasure(unit, jwtToken);
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
    public ResponseEntity<?> updateMeasure(
            @ApiParam(value = "", required = true) @Valid @RequestBody UnitOfKeyResult unit,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Update measure: " + unit.getName());
        try {
            return unitOfKeyResultService.updateMeasure(unit, jwtToken);
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
    public ResponseEntity<?> deleteMeasure(
            @ApiParam(value = "", required = true) @RequestParam(name = "id") long id,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Delete measure: " + id);
        try {
            return unitOfKeyResultService.deleteMeasure(id, jwtToken);
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
    public ResponseEntity<?> searchByName(
            @ApiParam(value = "", required = true) @RequestParam(name = "name") String name,
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "paging") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "size") int size,
            @ApiParam(value = "Kết quả trả về sắp xếp theo", required = true) @RequestParam(name = "sortWith") String sort,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Search measure: " + name);
        try {
            return unitOfKeyResultService.searchMeasure(name, page, size, sort, jwtToken);
        } catch (Exception e) {
            logger.error("Search measure failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}
