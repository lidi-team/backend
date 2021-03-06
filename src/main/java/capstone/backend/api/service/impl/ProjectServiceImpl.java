package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.AddStaffToProjectDto;
import capstone.backend.api.dto.CreateProjectDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.ApiResponse.Project.ProjectDetailResponse;
import capstone.backend.api.entity.ApiResponse.Project.ProjectListResponse;
import capstone.backend.api.entity.ApiResponse.Project.ProjectPagingResponse;
import capstone.backend.api.entity.ApiResponse.Project.StaffInformation;
import capstone.backend.api.entity.*;
import capstone.backend.api.repository.*;
import capstone.backend.api.service.ProjectService;
import capstone.backend.api.utils.CommonUtils;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    private final CommonProperties commonProperties;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private final ExecuteRepository executeRepository;

    private final ObjectiveRepository objectiveRepository;

    private final CommonUtils commonUtils;

    private final RoleRepository roleRepository;

    private final ProjectPositionRepository positionRepository;

    private final CycleRepository cycleRepository;

    @Override
    public ResponseEntity<?> getAllProjects() throws Exception {
        List<Project> projects = projectRepository.findAll();
        List<ProjectListResponse> projectListResponses = new ArrayList<>();
        projects.forEach(project -> {
            projectListResponses.add(
                    ProjectListResponse.builder()
                            .id(project.getId())
                            .name(project.getName())
                            .fromDate(project.getFromDate())
                            .toDate(project.getEndDate())
                            .build()

            );
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(projectListResponses).build()
        );
    }

    @Override
    public ResponseEntity<?> getListMetaDataProject() throws Exception {
        ArrayList<Project> projects = (ArrayList<Project>) projectRepository.findAllDeleteFalseAndCloseFalse();
        ArrayList<MetaDataResponse> responses = new ArrayList<>();

        projects.forEach(project -> {
            responses.add(
                    MetaDataResponse.builder()
                            .id(project.getId())
                            .name(project.getName())
                            .build()
            );
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses).build()
        );
    }

    @Override
    public ResponseEntity<?> getAllAvailableProjectOfUser(String token) throws Exception {
        List<MetaDataResponse> responses = new ArrayList<>();

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        List<Execute> executes = executeRepository.findAllByUserIdAndOpenProject(user.getId());
        executes.forEach(execute -> {
            if (execute.getProject() != null) {
                responses.add(
                        MetaDataResponse.builder()
                                .id(execute.getProject().getId())
                                .name(execute.getProject().getName())
                                .build()
                );
            }
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses).build()
        );
    }

    @Override
    public ResponseEntity<?> getAllProjectPaging(int page, int limit, String sortWith, String type, String text, String token) throws Exception {
        List<Project> projects;
        List<ProjectPagingResponse> list = new ArrayList<>();

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();
        List<Long> ids;
        if (user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("role_director")
                || role.getName().equalsIgnoreCase("role_admin"))) {
            ids = new ArrayList<>();
        } else {
            ids = projectRepository.findAllProjectOfUser(user.getId());
        }

        Map<String, Object> response = new HashMap<>();
        if (limit == 0) {
            limit = 10;
        }
        if (sortWith.equalsIgnoreCase("status")) {
            sortWith = "close";
        }
        if (text == null) {
            text = "";
        }

        if (type == null || type.equalsIgnoreCase("total") || type.isEmpty()) {
            projects = projectRepository.findAllByNameContains(text);
        } else {
            projects = projectRepository.findAllByCloseAndNameContains(
                    !type.equalsIgnoreCase("active"), text);
        }

        if (user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("role_director")
                || role.getName().equalsIgnoreCase("role_admin"))) {
            projects.forEach(project -> {
                Execute execute = executeRepository.findPmAllByProjectId(project.getId());
                list.add(
                        ProjectPagingResponse.builder()
                                .id(project.getId())
                                .name(project.getName())
                                .startDate(project.getFromDate())
                                .endDate(project.getEndDate())
                                .description(project.getDescription())
                                .isActive(!project.isClose())
                                .pmId(execute == null ? 0 : execute.getUser().getId())
                                .weight(project.getWeight())
                                .parentId(project.getParent() == null ? 0 : project.getParent().getId())
                                .build()
                );
            });
        } else {
            projects.forEach(project -> {
                Execute execute = executeRepository.findPmAllByProjectId(project.getId());
                if (ids.stream().anyMatch(id -> id == project.getId())) {
                    list.add(
                            ProjectPagingResponse.builder()
                                    .id(project.getId())
                                    .name(project.getName())
                                    .startDate(project.getFromDate())
                                    .endDate(project.getEndDate())
                                    .description(project.getDescription())
                                    .isActive(!project.isClose())
                                    .pmId(execute == null ? 0 : execute.getUser().getId())
                                    .weight(project.getWeight())
                                    .parentId(project.getParent() == null ? 0 : project.getParent().getId())
                                    .build()
                    );
                }
            });
        }



        PagedListHolder pages = new PagedListHolder(list);
        pages.setPageSize(limit);
        pages.setPage(page - 1);

        Map<String, Integer> meta = new HashMap<>();
        meta.put("totalItems", list.size());
        meta.put("currentPage", page);
        meta.put("totalPages", pages.getPageCount());
        response.put("data", pages.getPageList());
        response.put("meta", meta);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    @Override
    public ResponseEntity<?> getDetailProjectById(long id,String token) throws Exception {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        if (checkAuthorizedUser(id, token)) return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_UN_AUTHORIZED())
                        .message(commonProperties.getMESSAGE_UN_AUTHORIZED())
                        .build()
        );

        Execute pm = executeRepository.findPmByProjectId(id);
        Map<String, Object> pmResponse = new HashMap<>();
        pmResponse.put("id", pm.getUser().getId());
        pmResponse.put("name", pm.getUser().getFullName());
        pmResponse.put("position", pm.getPosition().getName());
        pmResponse.put("Department", pm.getUser().getDepartment().getName());

        ProjectDetailResponse response = ProjectDetailResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .isActive(!project.isClose())
                .startDate(project.getFromDate())
                .endDate(project.getEndDate())
                .pm(pmResponse)
                .weight(project.getWeight())
                .parentId(project.getParent() == null ? 0 : project.getParent().getId())
                .build();

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    private boolean checkAuthorizedUser(long id, String token) {
        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();
        if (!user.getRoles().stream().anyMatch(role ->
                role.getName().equalsIgnoreCase("ROLE_ADMIN")
                        || role.getName().equalsIgnoreCase("ROLE_DIRECTOR"))) {
            List<Execute> executes = executeRepository.getExecuteByUserIdAndProjectId(user.getId(), id);
            if (executes == null || executes.size() == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ResponseEntity<?> createProject(CreateProjectDto projectDto) throws Exception {
        Role rolePm = roleRepository.findRoleByName("ROLE_PM").get();
        ProjectPosition position = positionRepository.findById(1L).get();

        List<Project> projects;
        if(projectDto.getId() == 0){
            projects = projectRepository.findAllByDeleteFalse(projectDto.getName());
        }else {
            projects = projectRepository.findAllByDeleteFalseAndIdNotIn(projectDto.getName(),projectDto.getId());
        }
        if(projects != null && projects.size() > 0){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message("Tên dự án đã tồn tại trong hệ thống")
                            .data(projectDto.getId())
                            .build()
            );
        }

        Project parentProject = null;
        if (projectDto.getParentId() != 0) {
            parentProject = projectRepository.findById(projectDto.getParentId()).orElse(null);
        }

        String fromDateStr = projectDto.getStartDate();
        String endDateStr = projectDto.getEndDate();
        Date fromDate = commonUtils.stringToDate(fromDateStr, CommonUtils.PATTERN_ddMMyyyy);
        Date endDate = commonUtils.stringToDate(endDateStr, CommonUtils.PATTERN_ddMMyyyy);

        Project project;
        if (projectDto.getId() == 0) {
            project = Project.builder()
                    .name(projectDto.getName())
                    .parent(parentProject)
                    .close(!projectDto.isActive())
                    .fromDate(fromDate)
                    .endDate(endDate)
                    .description(projectDto.getDescription())
                    .isDelete(false)
                    .weight(projectDto.getWeight() == 0 ? 1 : projectDto.getWeight())
                    .build();
        } else {
            project = Project.builder()
                    .id(projectDto.getId())
                    .name(projectDto.getName())
                    .parent(parentProject)
                    .close(!projectDto.isActive())
                    .fromDate(fromDate)
                    .endDate(endDate)
                    .isDelete(false)
                    .description(projectDto.getDescription())
                    .weight(projectDto.getWeight() == 0 ? 1 : projectDto.getWeight())
                    .build();
        }


        User pm = userRepository.findById(projectDto.getPmId()).get();
        pm.getRoles().add(rolePm);
        pm = userRepository.save(pm);

        Execute execute = executeRepository.findPmAllByProjectId(project.getId());
        User director;
        if(project.getParent() != null){
            Execute execute1 = executeRepository.findPmByProjectId(project.getParent().getId());
            director = execute1.getUser();
        }else {
            director = userRepository.findDirector();
        }


        if (execute != null) {
            User oldUser = execute.getUser();
            // check if pm of project is changed
            if (oldUser.getId() != pm.getId()) {
                // check role PM of old pm -> decide to remove role pm or not
                if (executeRepository.findOtherProjectUserIsPm(oldUser.getId(), project.getId()) == 0) {
                    oldUser.getRoles().remove(rolePm);
                    userRepository.save(oldUser);
                }
                // check if new pm is in project
                Execute newExecute = executeRepository.findByProjectIdAndUserId(execute.getProject().getId(), pm.getId());
                if (newExecute != null) {
                    // add information of new pm
                    newExecute.setPosition(position);
                    newExecute.setPm(true);
                    newExecute.setReviewer(director);
                    executeRepository.save(newExecute);
                    // clear role old pm
                    execute.setPm(false);
                    execute.setPosition(null);

                } else {
                    // clear role old pm
                    execute.setPm(false);
                    execute.setPosition(null);

                    newExecute = Execute.builder()
                            .user(pm)
                            .project(project)
                            .fromDate(fromDate)
                            .endDate(endDate)
                            .isPm(true)
                            .isDelete(false)
                            .position(position)
                            .reviewer(director)
                            .build();

                }
                Cycle cycle = cycleRepository.findFirstByFromDateBeforeAndEndDateAfter(new Date(), new Date());
                List<Objective> objectives = objectiveRepository.findAllByExecuteIdAndCycleIdAndType(execute.getId(), cycle.getId(), 1);
                execute.setReviewer(pm);
                executeRepository.save(newExecute);
                for (Objective objective : objectives) {
                    objective.setExecute(newExecute);
                }
                objectiveRepository.saveAll(objectives);
            }
        } else {
            execute = Execute.builder()
                    .user(pm)
                    .fromDate(fromDate)
                    .endDate(endDate)
                    .isPm(true)
                    .isDelete(false)
                    .position(position)
                    .reviewer(director)
                    .build();
        }
        project = projectRepository.save(project);
        execute.setProject(project);
        executeRepository.save(execute);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(project.getId())
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getListParentProject() throws Exception {
        List<MetaDataResponse> responses = new ArrayList<>();

        List<Project> projects = projectRepository.findAllParentProject();
        projects.forEach(project -> {
            responses.add(
                    MetaDataResponse.builder()
                            .id(project.getId())
                            .name(project.getName())
                            .build()
            );
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getListStaffForPm(String text) throws Exception {
        List<MetaDataResponse> responses = new ArrayList<>();
        Role role = roleRepository.findRoleByName("ROLE_USER").get();
        List<User> users = userRepository.findByFullNameContains(text.toLowerCase());

        users.forEach(user -> {
            if (user.getRoles().contains(role)) {
                responses.add(
                        MetaDataResponse.builder()
                                .id(user.getId())
                                .name(user.getFullName())
                                .build()
                );
            }
        });

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> updateListStaff(List<AddStaffToProjectDto> dtos, long projectId) throws Exception {
        Project project = projectRepository.findById(projectId).orElse(null);
        if(project == null){
            return ResponseEntity.notFound().build();
        }
        List<Execute> oldStaffs = executeRepository.findAllStaffByProjectId(projectId);

        for (AddStaffToProjectDto dto : dtos) {
            boolean exist = false;
            for (Execute oldStaff : oldStaffs) {
                if (dto.getUserId() == oldStaff.getUser().getId()) {
                    if (dto.getReviewerId() != oldStaff.getReviewer().getId()) {
                        User reviewer = userRepository.findById(dto.getReviewerId()).get();
                        oldStaff.setReviewer(reviewer);
                    }
                    ProjectPosition position = positionRepository.findById(dto.getPositionId()).get();
                    oldStaff.setPosition(position);
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                User user = userRepository.findById(dto.getUserId()).get();
                User reviewer = userRepository.findById(dto.getReviewerId()).get();
                ProjectPosition position = positionRepository.findById(dto.getPositionId()).get();

                Execute execute = Execute.builder()
                        .user(user)
                        .reviewer(reviewer)
                        .position(position)
                        .fromDate(new Date())
                        .endDate(project.getEndDate())
                        .isPm(false)
                        .isDelete(false)
                        .build();
                oldStaffs.add(execute);
            }
        }

        executeRepository.saveAll(oldStaffs);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getListStaffByProjectId(long projectId, String token) throws Exception {
        List<StaffInformation> responses = new ArrayList<>();

        if (checkAuthorizedUser(projectId, token)) return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_UN_AUTHORIZED())
                        .message(commonProperties.getMESSAGE_UN_AUTHORIZED())
                        .build()
        );


        List<Execute> staffs = executeRepository.findAllStaffByProjectId(projectId);
        staffs.forEach(staff -> {
            responses.add(
                    StaffInformation.builder()
                            .id(staff.getUser().getId())
                            .name(staff.getUser().getFullName())
                            .department(staff.getUser().getDepartment().getId())
                            .position(staff.getPosition() == null ? 0 : staff.getPosition().getId())
                            .email(staff.getUser().getEmail())
                            .reviewerId(staff.getReviewer().getId())
                            .build()
            );
        });

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> addStaffToProject(List<Long> ids, long projectId) throws Exception {
        List<Execute> inserts = new ArrayList<>();
        List<User> staffs = userRepository.findAllByIdIn(ids);

        Project project = projectRepository.findById(projectId).orElse(null);
        if(project == null){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message("Không thể thêm nhân sự cho dự án")
                            .build()
            );
        }

        Execute pm = executeRepository.findPmByProjectId(projectId);

        staffs.forEach(staff -> {
            inserts.add(
                    Execute.builder()
                            .user(staff)
                            .isDelete(false)
                            .isPm(false)
                            .endDate(pm.getEndDate())
                            .fromDate(new Date())
                            .reviewer(pm.getUser())
                            .project(pm.getProject())
                            .build()
            );
        });

        executeRepository.saveAll(inserts);
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message("Thêm mới nhân sự thành công")
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> removeStaff(long projectId, long userId) throws Exception {

        Cycle cycle = cycleRepository.findFirstByFromDateBeforeAndEndDateAfter(new Date(), new Date());
        Execute execute = executeRepository.findByProjectIdAndUserId(projectId, userId);
        List<Objective> objectives = objectiveRepository.findAllByExecuteIdAndCycleIdAndType(execute.getId(),cycle.getId(),2);
        if(objectives != null && objectives.size() > 0){
            for (Objective objective : objectives) {
                objective.setStatus("Completed");
            }
            objectiveRepository.saveAll(objectives);
        }

        executeRepository.removeStaff(projectId, userId);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message("Đã xóa nhân viên khỏi dự án")
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getListCandidate(long projectId) throws Exception {
        Project project = projectRepository.findById(projectId).orElse(null);
        if(project == null){
            return ResponseEntity.notFound().build();
        }
        List<Long> memberIds = userRepository.findMemberProject(projectId);
        List<User> all = userRepository.findAllByIdNotIn(memberIds);
        List<Map<String, Object>> responses = new ArrayList<>();
        all.forEach(user -> {
            if (user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_USER"))) {
                Map<String, Object> candidate = new HashMap<>();
                candidate.put("id", user.getId());
                candidate.put("name", user.getFullName());
                candidate.put("email", user.getEmail());
                responses.add(candidate);
            }
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getAllProjectManagePaging(int page, int limit, String sortWith, String type, String text, String token) throws Exception {
        Page<Project> projects;
        List<ProjectPagingResponse> list = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        if (limit == 0) {
            limit = 10;
        }
        if (sortWith.equalsIgnoreCase("status")) {
            sortWith = "close";
        }
        if (text == null) {
            text = "";
        }

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        List<Long> ids = executeRepository.findAllProjectIdByUserId(user.getId());

        if (type == null || type.equalsIgnoreCase("total") || type.isEmpty()) {
            projects = projectRepository.findAllByNameContainsAndIdIn(text, ids, PageRequest.of(page - 1, limit, Sort.by(sortWith)));
        } else {
            projects = projectRepository.findAllByCloseAndNameContainsAndIdIn(
                    !type.equalsIgnoreCase("active"), text, ids,
                    PageRequest.of(page - 1, limit, Sort.by(sortWith)));
        }

        projects.getContent().forEach(project -> {
            Execute execute = executeRepository.findPmByProjectId(project.getId());
            list.add(
                    ProjectPagingResponse.builder()
                            .id(project.getId())
                            .name(project.getName())
                            .startDate(project.getFromDate())
                            .endDate(project.getEndDate())
                            .description(project.getDescription())
                            .isActive(!project.isClose())
                            .pmId(execute.getUser().getId())
                            .weight(project.getWeight())
                            .parentId(project.getParent() == null ? 0 : project.getParent().getId())
                            .build()
            );
        });
        response.put("data", list);
        response.put("meta", commonUtils.paging(projects, page));

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    @Override
    public ResponseEntity<?> deleteProject(long projectId) {

        Project project = projectRepository.findById(projectId).orElse(null);

        if(project == null){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }
        String message = "";
        if(project.isClose()){
            project.setClose(false);
            message = "Đã mở dự án thành công";
            executeRepository.updateAllStatusWhenOpenProject(projectId);
        } else {
            project.setClose(true);
            message = "Đã đóng dự án thành công";
            executeRepository.updateAllStatusWhenCloseProject(projectId);
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message(message).build()
        );
    }


}
