package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.DepartmentDto;
import capstone.backend.api.dto.EvaluationCriteriaDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.Department;
import capstone.backend.api.entity.EvaluationCriteria;
import capstone.backend.api.repository.EvaluationCriteriaRepository;
import capstone.backend.api.service.EvaluationCriteriaService;
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
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService {

    EvaluationCriteriaRepository evaluationRepository;

    private CommonProperties commonProperties;

    private final CommonUtils commonUtils;

    @Override
    public ResponseEntity<?> getListMetaDataEvaluation() throws Exception {
        List<EvaluationCriteria> criterias = evaluationRepository.findAll();

        ArrayList<MetaDataResponse> responses = new ArrayList<>();
        criterias.forEach(criteria -> {
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

    @Override
    public ResponseEntity<?> getAllEvaluation(int page, int limit, String text, String jwtToken) throws Exception {
        if (limit == 0){
            limit = 10;
        }

        Page<EvaluationCriteria> evaluationCriterias;

        if (text == null) {
            evaluationCriterias = evaluationRepository.findByIsDeleteFalse(PageRequest.of(page-1, limit, Sort.by("id")));
        }else{
            evaluationCriterias = evaluationRepository.findByContentContainsAndIsDeleteFalse(text, PageRequest.of(page-1, limit, Sort.by("id")));
        }

        Map<String, Object> responses = new HashMap<>();
        List<Object> items = new ArrayList<>();


        evaluationCriterias.forEach(evaluationCriteria -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", evaluationCriteria.getId());
            item.put("content", evaluationCriteria.getContent());
            item.put("type", evaluationCriteria.getType());
            item.put("numberOfStar", evaluationCriteria.getNumberOfStar());
            items.add(item);
        });
        responses.put("items", items);
        responses.put("meta", commonUtils.paging(evaluationCriterias, page));
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses).build()
        );
    }

    @Override
    public ResponseEntity<?> createEvaluation(EvaluationCriteriaDto evaluationCriteriaDto, String jwtToken) throws Exception {
        Object data = evaluationRepository.save(EvaluationCriteria.builder()
                .content(evaluationCriteriaDto.getContent())
                .numberOfStar(evaluationCriteriaDto.getNumberOfStar())
                .type(evaluationCriteriaDto.getType())
                .build());

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(data)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getEvaluation(long id, String jwtToken) throws Exception {
        EvaluationCriteria evaluationCriteria = evaluationRepository.findByIdAndIsDeleteFalse(id);

        if(evaluationCriteria == null){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        Map<String, Object> item = new HashMap<>();
        item.put("id", evaluationCriteria.getId());
        item.put("content", evaluationCriteria.getContent());
        item.put("type", evaluationCriteria.getType());
        item.put("numberOfStar", evaluationCriteria.getNumberOfStar());

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(item).build()
        );
    }

    @Override
    public ResponseEntity<?> updateEvaluation(long id, EvaluationCriteriaDto evaluationCriteriaDto, String jwtToken) throws Exception {
        EvaluationCriteria evaluationCriteria = evaluationRepository.findByIdAndIsDeleteFalse(id);

        if (evaluationCriteria == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );
        }

        evaluationRepository.save(EvaluationCriteria.builder()
                .id(id)
                .content(evaluationCriteriaDto.getContent())
                .type(evaluationCriteriaDto.getType())
                .numberOfStar(evaluationCriteriaDto.getNumberOfStar())
                .build());

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> deleteEvaluation(long id, String jwtToken) throws Exception {
        EvaluationCriteria evaluationCriteria = evaluationRepository.findByIdAndIsDeleteFalse(id);

        if (evaluationCriteria == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );
        }else {
            evaluationCriteria.setDelete(true);
            evaluationRepository.save(evaluationCriteria);
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }


}
