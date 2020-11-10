package capstone.backend.api.service;

import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ObjectiveService {
    ResponseEntity<?> addObjective(ObjectvieDto objectvieDto, String token) throws Exception;

    ResponseEntity<?> deleteObjective(long id);

    ResponseEntity<?> getListChildObjectiveByObjectiveId(long objectiveId, long cycleId) throws Exception;

    ResponseEntity<?> getListObjectiveTitleByUserId(long userId) throws Exception;

    ResponseEntity<?> getParentObjectiveTitleByObjectiveId(long id, String token) throws Exception;

    ResponseEntity<?> getParentKeyResultTitleByObjectiveId(long id) throws Exception;

    ResponseEntity<?> getListAlignByProjectIdAndCycleId(long projectId,long cycleId) throws Exception;

    ResponseEntity<?> getKeyResultTitleByObjectiveId(long id) throws Exception;

    ResponseEntity<?> getAllObjectiveAndProjectOfUser(String token,long id) throws Exception;

    ResponseEntity<?> getDetailObjectiveById(long id) throws Exception;

    ResponseEntity<?> getAlignObjectiveById(long id) throws Exception;
}
