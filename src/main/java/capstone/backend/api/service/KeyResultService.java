package capstone.backend.api.service;

import capstone.backend.api.dto.KeyResultDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.KeyResultResponse;
import capstone.backend.api.entity.KeyResult;
import capstone.backend.api.entity.Objective;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface KeyResultService {

    ArrayList<KeyResult> addKeyResults(List<KeyResultDto> keyResultDtos, Objective objective) throws Exception;

    void deleteKeyResultByObjectiveId(long id);

    ArrayList<KeyResult> getKeyResultsByObjectiveId(long id);

    boolean validateKeyResults(List<KeyResultDto> keyResultDtos);

    ResponseEntity<ApiResponse> deleteKeyResultById(long id);

}
