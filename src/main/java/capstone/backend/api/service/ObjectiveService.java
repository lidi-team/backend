package capstone.backend.api.service;

import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ObjectiveService {
    ResponseEntity<ApiResponse> addObjective(ObjectvieDto objectvieDto);

    ResponseEntity<ApiResponse> deleteObjective(long id);

    ResponseEntity<ApiResponse> getAllObjective();

    ResponseEntity<ApiResponse> getObjectiveByObjectiveId(long id);
}
