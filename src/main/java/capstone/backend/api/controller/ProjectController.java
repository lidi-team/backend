package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.AddStaffToProjectDto;
import capstone.backend.api.dto.CreateProjectDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.impl.ObjectiveServiceImpl;
import capstone.backend.api.service.impl.ProjectServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/projects")
public class ProjectController {

    private final CommonProperties commonProperties;

    private final ProjectServiceImpl projectService;

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveServiceImpl.class);

    @ApiOperation(value = "Tất cả project hiện tại")
    @GetMapping(path = "/all")
    public ResponseEntity<?> getAllProject(){
        try {
            return projectService.getAllProjects();
        } catch (Exception e) {
            logger.error("get list project failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Tất cả project hiện tại user đang tham gia")
    @GetMapping(path = "/available")
    public ResponseEntity<?> getAllCurrentProjectOfUser(
            @ApiParam(value = "token",required = true)
            @RequestHeader(name = "Authorization") String token){
        try {
            return projectService.getAllAvailableProjectOfUser(token);
        } catch (Exception e) {
            logger.error("get list project available of a user failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Tất cả project trong hệ thống")
    @GetMapping(path = "")
    public ResponseEntity<?> getAllProject(
            @ApiParam(value = "số thứ tự trang hiện tại")
            @RequestParam(value = "page") int page,
            @ApiParam(value = "tổng số item giới hạn trong trang")
            @RequestParam(value = "limit") int limit,
            @ApiParam(value = "filter sort theo thứ tự gì")
            @RequestParam(value = "sortWith") String sortWith,
            @ApiParam(value = "status của project, null = total")
            @RequestParam(value = "type") String type,
            @ApiParam(value = "name của project")
            @RequestParam(value = "text") String text){
        try {
            return projectService.getAllProjectPaging(page,limit,sortWith,type,text);
        } catch (Exception e) {
            logger.error("get list project failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "lấy thông tin của project theo id")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getProjectById(
            @ApiParam(value = "Id của project")
            @PathVariable(value = "id")long id){
        try {
            return projectService.getDetailProjectById(id);
        } catch (Exception e) {
            logger.error("get detail project failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "lấy thông tin của project theo id")
    @PostMapping(path = "/create")
    public ResponseEntity<?> createProject(
            @RequestBody() CreateProjectDto projectDto){
        try {
            return projectService.createProject(projectDto);
        } catch (Exception e) {
            logger.error("create project failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "lấy toàn bộ project có thể là parent project")
    @GetMapping(path = "/parents")
    public ResponseEntity<?> getListParentProject(){
        try {
            return projectService.getListParentProject();
        } catch (Exception e) {
            logger.error("get parent project failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "lấy toàn bộ project có thể là parent project")
    @GetMapping(path = "/pm")
    public ResponseEntity<?> getListStaffForPm(
            @ApiParam(value = "tên của staffs")
            @RequestParam(value = "text") String text){
        try {
            return projectService.getListStaffForPm(text);
        } catch (Exception e) {
            logger.error("get list staff failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "update toan bo staff cua project")
    @PutMapping(path = "/staff")
    public ResponseEntity<?> updateListStaff(
            @ApiParam(value = "list staff")
            @RequestBody() List<AddStaffToProjectDto> dto,
            @ApiParam(value = "id cua project")
            @RequestParam(value = "projectId")long projectId){
        try {
            return null;
        } catch (Exception e) {
            logger.error("get detail project failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }


}
