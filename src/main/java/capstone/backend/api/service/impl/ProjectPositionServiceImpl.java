package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.ProjectPosition;
import capstone.backend.api.repository.ProjectPositionRepository;
import capstone.backend.api.service.ProjectPositionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class ProjectPositionServiceImpl implements ProjectPositionService {

    private final CommonProperties commonProperties;

    private final ProjectPositionRepository positionRepository;

    @Override
    public ResponseEntity<ApiResponse> getListMetaDataPosition() throws Exception {
        ArrayList<ProjectPosition> positions = (ArrayList<ProjectPosition>) positionRepository.findAll();
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
}
