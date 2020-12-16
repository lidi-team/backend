package capstone.backend.api.service.impl;

import capstone.backend.api.dto.KeyResultDto;
import capstone.backend.api.entity.KeyResult;
import capstone.backend.api.entity.Objective;
import capstone.backend.api.entity.UnitOfKeyResult;
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

    private UnitOfKeyResultServiceImpl unitService;

    @Override
    public ArrayList<KeyResult> addKeyResults(List<KeyResultDto> keyResultDtos, Objective objective) throws Exception {

        ArrayList<KeyResult> keyResults = new ArrayList<>();

        keyResultDtos.forEach(keyResultDto -> {
            UnitOfKeyResult unit = unitService.getUnitById(keyResultDto.getMeasureUnitId());
            KeyResult keyResult;
            if(keyResultDto.getValueObtained() < keyResultDto.getStartValue()){
                keyResultDto.setValueObtained(keyResultDto.getStartValue());
            }
            if (keyResultDto.getId() == 0) {
                keyResult = new KeyResult().builder()
                        .fromValue(keyResultDto.getStartValue())
                        .valueObtained(keyResultDto.getValueObtained())
                        .toValue(keyResultDto.getTargetedValue())
                        .content(keyResultDto.getContent())
                        .objective(objective)
                        .unitOfKeyResult(unit)
                        .reference(keyResultDto.getReference())
                        .build();
            } else {
                keyResult = new KeyResult().builder()
                        .id(keyResultDto.getId())
                        .fromValue(keyResultDto.getStartValue())
                        .valueObtained(keyResultDto.getValueObtained())
                        .toValue(keyResultDto.getTargetedValue())
                        .content(keyResultDto.getContent())
                        .objective(objective)
                        .unitOfKeyResult(unit)
                        .reference(keyResultDto.getReference())
                        .build();
            }
            keyResults.add(keyResult);
        });

        ArrayList<KeyResult> keyResultOlds = keyResultRepository.findAllByObjectiveId(objective.getId());
        if (keyResultOlds != null) {
            for (int i = 0; i < keyResultOlds.size(); i++) {
                boolean check = true;
                for (KeyResult keyResult : keyResults) {
                    if (keyResultOlds.get(i).getId() == keyResult.getId()) {
                        check = false;
                        break;
                    }
                }
                if (!check) {
                    keyResultOlds.remove(keyResultOlds.get(i));
                }
            }
            keyResultOlds.forEach(keyResult -> {
                keyResult.setDelete(true);
            });
            keyResultRepository.saveAll(keyResultOlds);
        }
        return (ArrayList<KeyResult>) keyResultRepository.saveAll(keyResults);
    }

    @Override
    public void deleteKeyResultByObjectiveId(long id) {
        logger.info("deleted key result with objective id : " + id);
        keyResultRepository.updateKeyResultParentId(id);
        keyResultRepository.deleteKeyResultsByObjectiveId(id);
    }

    @Override
    public ArrayList<KeyResult> getKeyResultsByObjectiveId(long id) {
        return keyResultRepository.findAllByObjectiveId(id);
    }

    @Override
    public boolean validateKeyResults(List<KeyResultDto> keyResultDtos) {
        return !keyResultDtos.stream()
                .anyMatch(
                        keyResultDto -> keyResultDto.getContent().trim().isEmpty()
                );
    }


}
