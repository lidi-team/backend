package capstone.backend.api.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface EvaluationCriteriaService {

    ResponseEntity<?> getListMetaDataEvaluation(String type) throws Exception;
}
