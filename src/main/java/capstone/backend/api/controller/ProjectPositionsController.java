package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.CreateCycleDto;
import capstone.backend.api.dto.ProjectPositionDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.repository.DepartmentRepository;
import capstone.backend.api.repository.ProjectPositionRepository;
import capstone.backend.api.service.ProjectPositionService;
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
@RequestMapping(value = "/api/positions")
@AllArgsConstructor
public class ProjectPositionsController {

    private ProjectPositionService projectPositionService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private CommonProperties commonProperties;

    @ApiOperation(value = "lấy toàn bộ position, có phân trang")
    @GetMapping
    public ResponseEntity<?> viewListPosition(
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "page") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "limit") int size,
            @ApiParam(value = "Noi dung", required = false) @RequestParam(name = "text", required = false) String text,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return projectPositionService.getAllPosition(page, size, text, jwtToken);
        } catch (Exception e) {
            logger.error("get list position failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "tạo position mới")
    @PostMapping()
    public ResponseEntity<?> createPosition(
            @ApiParam(value = "", required = true)
            @Valid @RequestBody ProjectPositionDto positionDto,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return projectPositionService.createPosition(positionDto, jwtToken);
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

    @ApiOperation(value = "lấy thông tin chu kì theo id")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getPosition(
            @ApiParam(required = true)
            @PathVariable(value = "id") long id,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return projectPositionService.getPosition(id, jwtToken);
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "cập nhật thông tin position")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updatePosition(
            @ApiParam(required = true) @PathVariable(value = "id") long id,
            @ApiParam(required = true) @Valid @RequestBody ProjectPositionDto positionDto,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Update position: " + positionDto.getName());
        try {
            return projectPositionService.updatePosition(id, positionDto, jwtToken);
        } catch (Exception e) {
            logger.error("Update position failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "xoá một position theo id")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deletePosition(
            @ApiParam(required = true) @PathVariable(name = "id") long id,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Delete position: " + id);
        try {
            return projectPositionService.deletePosition(id, jwtToken);
        } catch (Exception e) {
            logger.error("Delete position failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}
