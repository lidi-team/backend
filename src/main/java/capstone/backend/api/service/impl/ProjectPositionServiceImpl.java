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

    private final CommonProperties commonProperties;

    private final ProjectPositionRepository positionRepository;

    private final CommonUtils commonUtils;

    @Override
    public ResponseEntity<ApiResponse> getListMetaDataPosition() throws Exception {
        ArrayList<ProjectPosition> positions = (ArrayList<ProjectPosition>) positionRepository.findAllDeleteFalse();
        ArrayList<MetaDataResponse> responses = new ArrayList<>();

        positions.forEach(projectPosition -> {
            if(!projectPosition.isDelete()){
                responses.add(
                        MetaDataResponse.builder()
                                .id(projectPosition.getId())
                                .name(projectPosition.getName())
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
    public ResponseEntity<ApiResponse> getAllPosition(int page, int limit, String text, String jwtToken) throws Exception {

        if (limit == 0){
            limit = 10;
        }

        Page<ProjectPosition> positions;

        if (text == null) {
            positions = positionRepository.findByIsDeleteFalse(PageRequest.of(page-1, limit, Sort.by("id")));
        }else{
            positions = positionRepository.findByNameContainsAndIsDeleteFalse(text, PageRequest.of(page-1, limit, Sort.by("id")));
        }


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

        ProjectPosition positionOld = positionRepository.findByName(positionDto.getName());
        if(positionOld!=null){
            if(positionOld.isDelete()){
                positionOld.setDelete(false);
                positionOld.setName(positionDto.getName());
                positionOld.setDescription(positionDto.getDescription());
                positionRepository.save(positionOld);
            }else{
                return ResponseEntity.ok().body(
                        ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                        .message("Vị trí này đã tồn tại").build()
                );
            }
        }else {
            positionRepository.save(ProjectPosition.builder()
                    .id(id)
                    .name(positionDto.getName())
                    .description(positionDto.getDescription())
                    .build());
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> deletePosition(long id, String jwtToken) throws Exception{
        ProjectPosition projectPosition = positionRepository.findByIdAndIsDeleteFalse(id);

        if(positionRepository.checkExisted(id).size() > 0){
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                            .message("Vị trí này đang được sử dụng").build()
            );
        }

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
