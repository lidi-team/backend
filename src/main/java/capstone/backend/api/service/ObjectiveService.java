package capstone.backend.api.service;

import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ObjectiveService {
    public ResponseEntity<ApiResponse> addObjective(ObjectvieDto objectvieDto);

    public ResponseEntity<ApiResponse> deleteObjective(long id);

    public ResponseEntity<ApiResponse> getAllObjective(long cycleId);

    public ResponseEntity<ApiResponse> getObjectiveByObjectiveId(long id);

    public ResponseEntity<ApiResponse> getObjectiveByUserId(long id);

    public ResponseEntity<ApiResponse> searchByCycleIdAndUserId(long cycleId, long userId);

    public ResponseEntity<ApiResponse> viewListObjectiveByCycleId(long cycleId);
}
