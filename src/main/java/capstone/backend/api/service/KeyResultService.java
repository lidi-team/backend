package capstone.backend.api.service;

import capstone.backend.api.dto.KeyResultDto;
import capstone.backend.api.entity.ApiResponse.KeyResultResponse;
import capstone.backend.api.entity.KeyResult;
import capstone.backend.api.entity.Objective;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface KeyResultService {

    ArrayList<KeyResult> addKeyResults(List<KeyResultDto> keyResultDtos, Objective objective);

    void deleteKeyResultByObjectiveId(long id);

    ArrayList<KeyResultResponse> getKeyResultsByObjectiveId(long id);

    boolean validateKeyResults(List<KeyResultDto> keyResultDtos);

}
