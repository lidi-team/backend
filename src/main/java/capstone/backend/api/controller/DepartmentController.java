package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.DepartmentDto;
import capstone.backend.api.dto.ProjectPositionDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.repository.DepartmentRepository;
import capstone.backend.api.service.DepartmentService;
import capstone.backend.api.service.ProjectPositionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/departments")
@AllArgsConstructor
public class DepartmentController {

    private DepartmentService departmentService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private CommonProperties commonProperties;

    @ApiOperation(value = "lấy toàn bộ Department, có phân trang")
    @GetMapping
    public ResponseEntity<?> viewListDepartment(
            @ApiParam(value = "Số trang cần truy vấn, trang đầu tiên là 0", required = true) @RequestParam(name = "page") int page,
            @ApiParam(value = "Số lượng kết quả trên mỗi trang, số nguyên", required = true) @RequestParam(name = "limit") int size,
            @ApiParam(value = "Kết quả trả về sắp xếp theo", required = false) @RequestParam(name = "sortWith", required = false) String sort,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return departmentService.getAllDepartment(page, size, sort, jwtToken);
        } catch (Exception e) {
            logger.error("get list Department failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "tạo Department mới")
    @PostMapping()
    public ResponseEntity<?> createDepartment(
            @ApiParam(value = "", required = true)
            @Valid @RequestBody DepartmentDto departmentDto,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return departmentService.createDepartment(departmentDto, jwtToken);
        } catch (Exception e) {
            logger.error("Create Department failed!");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "lấy thông tin Department theo id")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getDepartment(
            @ApiParam(required = true)
            @PathVariable(value = "id") long id,
            @RequestHeader(value = "Authorization") String jwtToken) {
        try {
            return departmentService.getDepartment(id, jwtToken);
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

    @ApiOperation(value = "cập nhật thông tin Department")
    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateDepartment(
            @ApiParam(required = true) @PathVariable(value = "id") long id,
            @ApiParam(required = true) @Valid @RequestBody DepartmentDto departmentDto,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Update position: " + departmentDto.getName());
        try {
            return departmentService.updateDepartment(id, departmentDto, jwtToken);
        } catch (Exception e) {
            logger.error("Update Department failed!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "xoá một Department theo id")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteDepartment(
            @ApiParam(required = true) @PathVariable(name = "id") long id,
            @RequestHeader(value = "Authorization") String jwtToken) {
        logger.info("Delete Department: " + id);
        try {
            return departmentService.deleteDepartment(id, jwtToken);
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
