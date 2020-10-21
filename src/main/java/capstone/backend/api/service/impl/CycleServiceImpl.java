package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.Cycle;
import capstone.backend.api.entity.ProjectPosition;
import capstone.backend.api.repository.CycleRepository;
import capstone.backend.api.service.CycleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CycleServiceImpl implements CycleService {

    private CycleRepository cycleRepository;

    private CommonProperties commonProperties;

    @Override
    public Cycle getCycleById(long id) throws Exception {
        return cycleRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<ApiResponse> getListMetaDataCycle() {
        List<Cycle> cycles = cycleRepository.findAll();
        ArrayList<MetaDataResponse> responses = new ArrayList<>();

        cycles.forEach(cycle -> {
            responses.add(
                    MetaDataResponse.builder()
                            .id(cycle.getId())
                            .name(cycle.getName())
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
