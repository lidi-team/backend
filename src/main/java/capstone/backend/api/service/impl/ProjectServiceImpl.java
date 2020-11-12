package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.ApiResponse.Project.ProjectListResponse;
import capstone.backend.api.entity.ApiResponse.Project.ProjectPagingResponse;
import capstone.backend.api.entity.Execute;
import capstone.backend.api.entity.Project;
import capstone.backend.api.entity.User;
import capstone.backend.api.repository.ExecuteRepository;
import capstone.backend.api.repository.ProjectRepository;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.ProjectService;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    ProjectRepository projectRepository;

    private final CommonProperties commonProperties;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private final ExecuteRepository executeRepository;

    @Override
    public ResponseEntity<ApiResponse> getAllProjects() throws Exception {
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
    public ResponseEntity<ApiResponse> getListMetaDataProject() throws Exception {
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
    public ResponseEntity<ApiResponse> getAllAvailableProjectOfUser(String token, int type) throws Exception {
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
            Execute execute = executeRepository.findByProjectIdAndIsPm(project.getId());
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
}
