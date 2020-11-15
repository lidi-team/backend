package capstone.backend.api.service;

import capstone.backend.api.dto.DepartmentDto;
import capstone.backend.api.dto.EvaluationCriteriaDto;
import capstone.backend.api.entity.EvaluationCriteria;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface EvaluationCriteriaService {

    ResponseEntity<?> getListMetaDataEvaluation() throws Exception;

    ResponseEntity<?> getAllEvaluation(int page, int limit, String sort, String jwtToken) throws Exception;

    ResponseEntity<?> createEvaluation(EvaluationCriteriaDto evaluationCriteriaDto, String jwtToken) throws Exception;

    ResponseEntity<?> getEvaluation(long id, String jwtToken) throws Exception;

    ResponseEntity<?> updateEvaluation(long id, EvaluationCriteriaDto evaluationCriteriaDto, String jwtToken) throws Exception;

    ResponseEntity<?> deleteEvaluation(long id, String jwtToken) throws Exception;
}
