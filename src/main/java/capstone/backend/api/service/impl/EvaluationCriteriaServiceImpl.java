package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.EvaluationCriteria;
import capstone.backend.api.repository.EvaluationCriteriaRepository;
import capstone.backend.api.service.EvaluationCriteriaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService {

    EvaluationCriteriaRepository evaluationRepository;

    private CommonProperties commonProperties;

    @Override
    public ResponseEntity<?> getListMetaDataEvaluation() throws Exception {
        List<EvaluationCriteria> criterias = evaluationRepository.findAll();

        ArrayList<MetaDataResponse> responses = new ArrayList<>();
        criterias.forEach(criteria ->{
            responses.add(
                    MetaDataResponse.builder()
                            .id(criteria.getId())
                            .name(criteria.getContent())
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
