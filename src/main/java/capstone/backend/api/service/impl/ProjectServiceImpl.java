package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.CreateProjectDto;
import capstone.backend.api.entity.*;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.ApiResponse.Project.ProjectDetailResponse;
import capstone.backend.api.entity.ApiResponse.Project.ProjectListResponse;
import capstone.backend.api.entity.ApiResponse.Project.ProjectPagingResponse;
import capstone.backend.api.entity.ApiResponse.Project.StaffInformation;
import capstone.backend.api.repository.*;
import capstone.backend.api.service.ProjectService;
import capstone.backend.api.utils.CommonUtils;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.*;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    ProjectRepository projectRepository;

    private final CommonProperties commonProperties;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private final ExecuteRepository executeRepository;

    private final CommonUtils commonUtils;

    private final RoleRepository roleRepository;

    private final ProjectPositionRepository positionRepository;

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
        ArrayList<Project> projects = (ArrayList<Project>) projectRepository.findAll();
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
    public ResponseEntity<?> getAllAvailableProjectOfUser(String token, int type) throws Exception {
        List<MetaDataResponse> responses = new ArrayList<>();

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        if (user.getRoles().stream().noneMatch(
                role -> role.getName().equals("ROLE_PM")) && type == 1) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_SUCCESS())
                            .message(commonProperties.getMESSAGE_SUCCESS())
                            .data(responses).build()
            );
        }

        List<Execute> executes = executeRepository.findAllByUserIdAndOpenProject(user.getId());
        if (type == 1) {
            executes.forEach(execute -> {
                if (execute.getProject() != null && execute.getPosition().getName()
                        .equalsIgnoreCase("project manager")) {
                    responses.add(
                            MetaDataResponse.builder()
                                    .id(execute.getProject().getId())
                                    .name(execute.getProject().getName())
                                    .build()
                    );
                }
            });
        } else if(type == 2){
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
        }
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses).build()
        );
    }

    @Override
    public ResponseEntity<?> getAllProjectPaging(int page, int limit, String sortWith, String type) throws Exception {
        Page<Project> projects;
        List<ProjectPagingResponse> list = new ArrayList<>();
        Map<String, Object> response = new HashMap<>();
        if (limit == 0){
            limit = 10;
        }

        if(type == null || type.equalsIgnoreCase("total") || type.isEmpty()){
            projects = projectRepository.findAll(PageRequest.of(page,limit, Sort.by(sortWith)));
        }else{
            projects = projectRepository.findAllByClose(
                    !type.equalsIgnoreCase("active"),
                    PageRequest.of(page,limit, Sort.by(sortWith)));
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
                            .status(project.isClose()?"Closed":"Active")
                            .pm(execute.getUser().getFullName())
                            .build()
            );
        });
        response.put("data", list);
        Map<String, Integer> meta = new HashMap<>();
        meta.put("totalItems", (int) projects.getTotalElements());
        meta.put("totalPages", projects.getTotalPages());
        response.put("meta", meta);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    @Override
    public ResponseEntity<?> getDetailProjectById(long id) throws Exception {
        Project project = projectRepository.findById(id).get();

        Execute pm = executeRepository.findPmByProjectId(id);
        StaffInformation pmResponse = StaffInformation.builder()
                .id(pm.getUser().getId())
                .name(pm.getUser().getFullName())
                .position(pm.getPosition().getName())
                .department(pm.getUser().getDepartment().getName())
                .build();
        List<StaffInformation> staffResponse = new ArrayList<>();
        List<Execute> staffs = executeRepository.findAllStaffByProjectId(id);
        staffs.forEach(staff->{
            staffResponse.add(
                    StaffInformation.builder()
                            .id(staff.getUser().getId())
                            .name(staff.getUser().getFullName())
                            .department(staff.getUser().getDepartment().getName())
                            .position(staff.getPosition().getName())
                            .build()
            );
        });

        ProjectDetailResponse response = ProjectDetailResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.isClose()?"Closed":"Active")
                .startDate(project.getFromDate())
                .endDate(project.getEndDate())
                .pm(pmResponse)
                .staffs(staffResponse)
                .build();

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    @Override
    public ResponseEntity<?> createProject(CreateProjectDto projectDto) throws Exception {
        Role rolePm = roleRepository.findRoleByName("ROLE_PM").get();
        ProjectPosition position = positionRepository.findById(1L).get();
        Project parentProject = projectRepository.findById(projectDto.getParentProjectId()).orElse(null);

        String fromDateStr = projectDto.getStartDate();
        String endDateStr = projectDto.getEndDate();
        Date fromDate = commonUtils.stringToDate(fromDateStr,CommonUtils.PATTERN_ddMMyyyy);
        Date endDate = commonUtils.stringToDate(endDateStr,CommonUtils.PATTERN_ddMMyyyy);

        Project project;
        if(projectDto.getId() == 0){
            project = Project.builder()
                    .name(projectDto.getName())
                    .parent(parentProject)
                    .close(projectDto.getStatus() == 0)
                    .fromDate(fromDate)
                    .endDate(endDate)
                    .description(projectDto.getDescription())
                    .build();
        }else{
            project = Project.builder()
                    .id(projectDto.getId())
                    .name(projectDto.getName())
                    .parent(parentProject)
                    .close(projectDto.getStatus() == 0)
                    .fromDate(fromDate)
                    .endDate(endDate)
                    .description(projectDto.getDescription())
                    .build();
        }
        project = projectRepository.save(project);

        User pm = userRepository.findById(projectDto.getPmId()).get();
        pm.getRoles().add(rolePm);
        pm = userRepository.save(pm);

        Execute execute = executeRepository.findPmByProjectId(project.getId());


        if(execute != null ){
            User oldUser = execute.getUser();
            // check if pm of project is changed
            if(oldUser.getId() != pm.getId()){
                // check role PM of old pm -> decide to remove role pm or not
                if(executeRepository.findOtherProjectUserIsPm(oldUser.getId(),project.getId()) == 0){
                    oldUser.getRoles().remove(rolePm);
                    userRepository.save(oldUser);
                }
            }
            execute = Execute.builder()
                    .id(execute.getId())
                    .user(pm)
                    .project(project)
                    .fromDate(fromDate)
                    .endDate(endDate)
                    .isPm(true)
                    .isDelete(false)
                    .position(position)
                    .build();
        }else{
            execute = Execute.builder()
                    .user(pm)
                    .project(project)
                    .fromDate(fromDate)
                    .endDate(endDate)
                    .isPm(true)
                    .isDelete(false)
                    .position(position)
                    .build();
        }
        executeRepository.save(execute);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }
}
