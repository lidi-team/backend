package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.ProjectPositionDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.ProjectPosition;
import capstone.backend.api.repository.ProjectPositionRepository;
import capstone.backend.api.service.ProjectPositionService;
import capstone.backend.api.utils.CommonUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ProjectPositionServiceImpl implements ProjectPositionService {

    private CommonProperties commonProperties;

    private ProjectPositionRepository positionRepository;

    private final CommonUtils commonUtils;

    @Override
    public ResponseEntity<ApiResponse> getListMetaDataPosition() throws Exception {
        ArrayList<ProjectPosition> positions = (ArrayList<ProjectPosition>) positionRepository.findAll();
        ArrayList<MetaDataResponse> responses = new ArrayList<>();

        positions.forEach(projectPosition -> {
            responses.add(
                    MetaDataResponse.builder()
                            .id(projectPosition.getId())
                            .name(projectPosition.getName())
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
    public ResponseEntity<ApiResponse> getAllPosition(int page, int limit, String sort, String jwtToken) throws Exception {

        if (limit == 0){
            limit = 10;
        }

        Page<ProjectPosition> positions = positionRepository.findByIsDeleteFalse(PageRequest.of(page-1, limit, Sort.by(sort)));
        Map<String, Object> responses = new HashMap<>();
        List<Object> items = new ArrayList<>();


        positions.forEach(projectPosition -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", projectPosition.getId());
            item.put("name", projectPosition.getName());
            item.put("description", projectPosition.getDescription());
            items.add(item);
        });
        responses.put("items", items);
        responses.put("meta", commonUtils.paging(positions, page));
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses).build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> createPosition(ProjectPositionDto positionDto, String jwtToken) throws Exception{

        Object data = positionRepository.save(ProjectPosition.builder()
                .name(positionDto.getName())
                .description(positionDto.getDescription())
                .build());

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(data)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> updatePosition(long id, ProjectPositionDto positionDto, String jwtToken) throws Exception{
        ProjectPosition projectPosition = positionRepository.findByIdAndIsDeleteFalse(id);

        if (projectPosition == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );
        }

        positionRepository.save(ProjectPosition.builder()
                .id(id)
                .name(positionDto.getName())
                .description(positionDto.getDescription())
                .build());

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> deletePosition(long id, String jwtToken) throws Exception{
        ProjectPosition projectPosition = positionRepository.findByIdAndIsDeleteFalse(id);

        if (projectPosition == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );
        }else {
            projectPosition.setDelete(true);
            positionRepository.save(projectPosition);
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getPosition(long id, String jwtToken) {
        ProjectPosition projectPosition = positionRepository.findByIdAndIsDeleteFalse(id);

        if(projectPosition == null){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        Map<String, Object> item = new HashMap<>();
        item.put("id", projectPosition.getId());
        item.put("name", projectPosition.getName());
        item.put("description", projectPosition.getDescription());

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(item).build()
        );
    }
}
