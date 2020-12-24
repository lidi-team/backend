package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.MeasureDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.UnitOfKeyResult;
import capstone.backend.api.service.UnitOfKeyResultService;
import capstone.backend.api.service.impl.ObjectiveServiceImpl;
import capstone.backend.api.service.impl.UnitOfKeyResultServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/measures")
public class MeasureController {
    private CommonProperties commonProperties;
    private static final Logger logger = LoggerFactory.getLogger(MeasureController.class);
    private UnitOfKeyResultService unitOfKeyResultService;
    @PreAuthorize("hasRole(commonProperties.getROLE_ADMIN())")
    @ApiOperation(value = "lấy toàn bộ measure, có phân trang")
    @GetMapping()
    public ResponseEntity<?> viewListCycles(
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "page") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "limit") int limit,
            @ApiParam(value = "Noi dung", required = false) @RequestParam(name = "text", required = false) String text,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return unitOfKeyResultService.getAllMeasure(page, limit, text, jwtToken);
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
    @PreAuthorize("hasRole(commonProperties.getROLE_ADMIN())")
    @ApiOperation(value = "tạo measure")
    @PostMapping()
    public ResponseEntity<?> createMeasure(
            @ApiParam(value = "", required = true)
            @Valid @RequestBody MeasureDto unit,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Create measure: " + unit.getType());
        try {
            return unitOfKeyResultService.createMeasure(unit, jwtToken);
        } catch (Exception e) {
            logger.error("Create cycle failed!");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message("Tạo Đơn vị đo không thành công").build()
            );
        }
    }

    @ApiOperation(value = "lấy thông tin measure theo id")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getDepartment(
            @ApiParam(required = true)
            @PathVariable(value = "id") long id,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return unitOfKeyResultService.getMeasureById(id, jwtToken);
        } catch (Exception e) {
            logger.error("get measure failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
    @PreAuthorize("hasRole(commonProperties.getROLE_ADMIN())")
    @ApiOperation(value = "cập nhật measure theo id")
    @PutMapping(path= "/{id}")
    public ResponseEntity<?> updateMeasure(
            @ApiParam(required = true) @PathVariable(value = "id") long id,
            @ApiParam(value = "", required = true) @Valid @RequestBody MeasureDto unit,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Update measure: " + unit.getType());
        try {
            return unitOfKeyResultService.updateMeasure(id, unit, jwtToken);
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
    @ApiOperation(value = "xoá measure theo id")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteMeasure(
            @ApiParam(required = true) @PathVariable(name = "id") long id,
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

    @ApiOperation(value = "tìm kiếm measure, có sắp xếp")
    @GetMapping(path = "search")
    public ResponseEntity<?> searchByName(
            @ApiParam(value = "", required = true) @RequestParam(name = "name") String name,
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "paging") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "limit") int limit,
            @ApiParam(value = "Kết quả trả về sắp xếp theo", required = false) @RequestParam(name = "sortWith", required = false) String sort,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Search measure: " + name);
        try {
            return unitOfKeyResultService.searchMeasure(name, page, limit, sort, jwtToken);
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
