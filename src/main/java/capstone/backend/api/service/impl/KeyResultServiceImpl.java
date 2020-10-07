package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.KeyResultDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.KeyResultResponse;
import capstone.backend.api.entity.KeyResult;
import capstone.backend.api.entity.MeasureUnit;
import capstone.backend.api.entity.Objective;
import capstone.backend.api.repository.KeyResultRepository;
import capstone.backend.api.repository.MeasureUnitRepository;
import capstone.backend.api.service.KeyResultService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class KeyResultServiceImpl implements KeyResultService {

    private static final Logger logger = LoggerFactory.getLogger(KeyResultServiceImpl.class);

    private KeyResultRepository keyResultRepository;

    private MeasureUnitRepository measureUnitRepository;

    private CommonProperties commonProperties;

    @Override
    public ArrayList<KeyResult> addKeyResults(List<KeyResultDto> keyResultDtos, Objective objective) {

        ArrayList<KeyResult> keyResults = new ArrayList<>();

        keyResultDtos.forEach(keyResultDto -> {
            MeasureUnit measureUnit = measureUnitRepository.findById(keyResultDto.getMeasureUnitId()).get();
            KeyResult keyResult;
            if (keyResultDto.getId() == 0) {
                keyResult = new KeyResult().builder()
                        .startValue(keyResultDto.getStartValue())
                        .valueObtained(keyResultDto.getValueObtained())
                        .targetedValue(keyResultDto.getTargetedValue())
                        .content(keyResultDto.getContent())
                        .objective(objective)
                        .measureUnit(measureUnit)
                        .common(keyResultDto.getCommon())
                        .build();
            } else {
                keyResult = new KeyResult().builder()
                        .id(keyResultDto.getId())
                        .startValue(keyResultDto.getStartValue())
                        .valueObtained(keyResultDto.getValueObtained())
                        .targetedValue(keyResultDto.getTargetedValue())
                        .content(keyResultDto.getContent())
                        .objective(objective)
                        .measureUnit(measureUnit)
                        .common(keyResultDto.getCommon())
                        .build();
            }
            keyResults.add(keyResult);
        });

        ArrayList<KeyResult> keyResultOlds = keyResultRepository.getKeyResultsByObjectiveId(objective.getId());
        if (keyResultOlds != null) {
            for (int i = 0; i < keyResultOlds.size(); i++) {
                boolean check = true;
                for (KeyResult keyResult : keyResults) {
                    if (keyResultOlds.get(i).getId() == keyResult.getId()) {
                        check = false;
                        break;
                    }
                }
                if(!check){
                    keyResultOlds.remove(keyResultOlds.get(i));
                }
            }
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
                        .common(keyResult.getCommon())
                        .measureUnitId(keyResult.getMeasureUnit().getId())
                        .startValue(keyResult.getStartValue())
                        .valueObtained(keyResult.getValueObtained())
                        .targetedValue(keyResult.getTargetedValue())
                        .content(keyResult.getContent())
                        .build());
            });
        }
        return keyResultResponses;
    }


    @Override
    public ResponseEntity<ApiResponse> deleteKeyResultById(long id) {
        keyResultRepository.deleteKeyResultsById(id);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public boolean validateKeyResults(List<KeyResultDto> keyResultDtos) {
        return !keyResultDtos.stream()
                .anyMatch(
                        keyResultDto -> keyResultDto.getContent().trim().isEmpty()
                );
    }
}
