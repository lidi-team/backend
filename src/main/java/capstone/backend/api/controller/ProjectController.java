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
            @RequestParam(value = "text") String text,
            @ApiParam(value = "token")
            @RequestHeader(value = "Authorization") String token){
        try {
            return projectService.getAllProjectPaging(page,limit,sortWith,type,text,token);
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

    @ApiOperation(value = "tao moi project")
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
                            .code(commonProperties.getCODE_UPDATE_FAILED())
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
    @PutMapping(path = "{projectId}/staff")
    public ResponseEntity<?> updateListStaff(
            @ApiParam(value = "list staff")
            @RequestBody() List<AddStaffToProjectDto> dto,
            @ApiParam(value = "id cua project")
            @PathVariable(value = "projectId")long projectId){
        try {
            return projectService.updateListStaff(dto,projectId);
        } catch (Exception e) {
            logger.error("get detail project failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "add staff vao project")
    @PostMapping(path = "/{projectId}/staff")
    public ResponseEntity<?> addStaffToProject(
            @ApiParam(value = "list staff")
            @RequestBody() List<Long> dtos,
            @ApiParam(value = "id cua project")
            @PathVariable(value = "projectId")long projectId){
        try {
            return projectService.addStaffToProject(dtos,projectId);
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

    @ApiOperation(value = "lấy toàn bộ staff cua project")
    @GetMapping(path = "/{projectId}/staff")
    public ResponseEntity<?> getListStaffByProjectId(
            @ApiParam(value = "id cua project")
            @PathVariable(value = "projectId") long projectId,
            @RequestHeader(value = "Authorization") String token){
        try {
            return projectService.getListStaffByProjectId(projectId, token);
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

    @ApiOperation(value = "xoa 1 user khoi project")
    @DeleteMapping(path = "/{projectId}/staff/{userId}")
    public ResponseEntity<?> removeStaffFromProject(
            @ApiParam(value = "id cua project")
            @PathVariable(value = "projectId") long projectId,
            @ApiParam(value = "id cua user")
            @PathVariable(value = "userId") long userId){
        try {
            return projectService.removeStaff(projectId, userId);
        } catch (Exception e) {
            logger.error("remove staff failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "lấy toàn bộ staff cua project")
    @GetMapping(path = "/{projectId}/candidate")
    public ResponseEntity<?> getListStaffCanAddToProject(
            @ApiParam(value = "id cua project")
            @PathVariable(value = "projectId") long projectId){
        try {
            return projectService.getListCandidate(projectId);
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

    @ApiOperation(value = "lấy toàn bộ staff cua project")
    @GetMapping(path = "/manage")
    public ResponseEntity<?> getListProjectManage(
            @ApiParam(value = "số thứ tự trang hiện tại")
            @RequestParam(value = "page") int page,
            @ApiParam(value = "tổng số item giới hạn trong trang")
            @RequestParam(value = "limit") int limit,
            @ApiParam(value = "filter sort theo thứ tự gì")
            @RequestParam(value = "sortWith") String sortWith,
            @ApiParam(value = "status của project, null = total")
            @RequestParam(value = "type") String type,
            @ApiParam(value = "name của project")
            @RequestParam(value = "text") String text,
            @ApiParam(value = "token",required = true)
            @RequestHeader(name = "Authorization") String token
    ){
        try {
            return projectService.getAllProjectManagePaging(page, limit, sortWith, type, text, token);
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

    @ApiOperation(value = "Deactive project")
    @DeleteMapping(path = "/{projectId}")
    public ResponseEntity<?> deleteProject(
            @ApiParam(value = "id cua project")
            @PathVariable(value = "projectId") long projectId){
        try {
            return projectService.deleteProject(projectId);
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

}
