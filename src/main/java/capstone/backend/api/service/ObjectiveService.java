package capstone.backend.api.service;

import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ObjectiveService {
    ResponseEntity<ApiResponse> addObjective(ObjectvieDto objectvieDto) throws Exception;

    ResponseEntity<ApiResponse> deleteObjective(long id);

    ResponseEntity<ApiResponse> getListChildObjectiveByObjectiveId(long objectiveId,long cycleId) throws Exception;

    ResponseEntity<ApiResponse> getListObjectiveTitleByUserId(long userId) throws Exception;

    ResponseEntity<ApiResponse> getParentObjectiveTitleByObjectiveId(long id) throws Exception;

    ResponseEntity<ApiResponse> getParentKeyResultTitleByObjectiveId(long id) throws Exception;
}
