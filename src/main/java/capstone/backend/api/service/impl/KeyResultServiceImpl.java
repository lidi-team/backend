package capstone.backend.api.service.impl;

import capstone.backend.api.dto.KeyResultDto;
import capstone.backend.api.entity.ApiResponse.KeyResultResponse;
import capstone.backend.api.entity.KeyResult;
import capstone.backend.api.entity.Objective;
import capstone.backend.api.repository.KeyResultRepository;
import capstone.backend.api.service.KeyResultService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class KeyResultServiceImpl implements KeyResultService {

    private static final Logger logger = LoggerFactory.getLogger(KeyResultServiceImpl.class);

    private KeyResultRepository keyResultRepository;

    @Override
    public ArrayList<KeyResult> addKeyResults(List<KeyResultDto> keyResultDtos, Objective objective) {

        ArrayList<KeyResult> keyResults = new ArrayList<>();

        keyResultDtos.forEach(keyResultDto -> {
            KeyResult keyResult;
            if (keyResultDto.getId() == 0) {
                keyResult = new KeyResult().builder()
                        .content(keyResultDto.getContent())
                        .objective(objective)
                        .build();
            } else {
                keyResult = new KeyResult().builder()
                        .id(keyResultDto.getId())
                        .content(keyResultDto.getContent())
                        .objective(objective)
                        .build();
            }
            keyResults.add(keyResult);
        });

        ArrayList<KeyResult> keyResultOlds = keyResultRepository.getKeyResultsByObjectiveId(objective.getId());
        if (keyResultOlds != null) {
            keyResultOlds.forEach(keyResultOld -> {
                boolean check = keyResults.stream().anyMatch(
                        keyResult -> {
                            return keyResultOld.getId() == keyResult.getId();
                        });
                if (check == true) {
                    keyResultOlds.remove(keyResultOld);
                }
            });
            keyResultRepository.deleteInBatch(keyResultOlds);
        }
        return (ArrayList<KeyResult>) keyResultRepository.saveAll(keyResults);
    }

    @Override
    public void deleteKeyResultByObjectiveId(long id) {
        logger.info("deleted key result with objective id : " + id);
        keyResultRepository.deleteKeyResultsByObjectiveId(id);
    }

    @Override
    public ArrayList<KeyResultResponse> getKeyResultsByObjectiveId(long id) {
        ArrayList<KeyResultResponse> keyResultResponses = new ArrayList<>();
        ArrayList<KeyResult> keyResults = keyResultRepository.getKeyResultsByObjectiveId(id);

        if (keyResults != null) {
            keyResults.forEach(keyResult -> {
                keyResultResponses.add(new KeyResultResponse().builder()
                        .id(keyResult.getId())
                        .content(keyResult.getContent())
                        .build());
            });
        }
        return keyResultResponses;
    }

    @Override
    public boolean validateKeyResults(List<KeyResultDto> keyResultDtos) {
        return !keyResultDtos.stream()
                .anyMatch(
                        keyResultDto -> keyResultDto.getContent().trim().isEmpty()
                );
    }


}
