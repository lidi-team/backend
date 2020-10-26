package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.ApiResponse.ProjectListResponse;
import capstone.backend.api.entity.Project;
import capstone.backend.api.repository.ProjectRepository;
import capstone.backend.api.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    ProjectRepository projectRepository;

    private CommonProperties commonProperties;

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
}
