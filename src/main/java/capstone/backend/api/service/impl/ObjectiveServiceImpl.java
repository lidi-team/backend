package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.KeyResultResponse;
import capstone.backend.api.entity.ApiResponse.ObjectiveResponse;
import capstone.backend.api.entity.KeyResult;
import capstone.backend.api.entity.Objective;
import capstone.backend.api.entity.User;
import capstone.backend.api.repository.ObjectiveRepository;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.ObjectiveService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ObjectiveServiceImpl implements ObjectiveService {

    private CommonProperties commonProperties;

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveServiceImpl.class);

    private UserRepository userRepository;

    private ObjectiveRepository objectiveRepository;

    private KeyResultServiceImpl keyResultService;

    @Override
    public ResponseEntity<ApiResponse> addObjective(ObjectvieDto objectvieDto) {
        if (!validateObjectiveInformation(objectvieDto)) {
            logger.error("Parameter is empty!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }

        User user = userRepository.findById(objectvieDto.getUserId()).get();
        Objective objective;
        if (objectvieDto.getId() == 0) {
            objective = Objective.builder()
                    .name(objectvieDto.getTitle())
                    .content(objectvieDto.getContent())
                    .user(user).build();
        } else {
            objective = Objective.builder()
                    .id(objectvieDto.getId())
                    .name(objectvieDto.getTitle())
                    .content(objectvieDto.getContent())
                    .user(user).build();
        }
        objective = objectiveRepository.save(objective);
        logger.info("save objective successful");

        ArrayList<KeyResultResponse> keyResultResponses = new ArrayList<>();

        if (objectvieDto.getKeyResults() != null) {
            if (!keyResultService.validateKeyResults(objectvieDto.getKeyResults())) {
                logger.error("Parameter is empty!");
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                                .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
                );
            } else {
                ArrayList<KeyResult> keyResults =
                        keyResultService.addKeyResults(objectvieDto.getKeyResults(), objective);
                keyResults.forEach(keyResult -> {
                    keyResultResponses.add(
                            KeyResultResponse.builder()
                                    .id(keyResult.getId())
                                    .content(keyResult.getContent())
                                    .build());
                });
                logger.info("save key results successful");
            }
        }

        ObjectiveResponse objectiveResponse = ObjectiveResponse.builder().id(objective.getId())
                .title(objective.getName())
                .content(objective.getContent())
                .userId(objective.getUser().getId())
                .keyResults(keyResultResponses)
                .build();

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(objectiveResponse).build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> deleteObjective(long id) {

        Objective objective = objectiveRepository.findById(id).orElse(null);
        if (objective == null) {
            logger.error("Objective not found!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        keyResultService.deleteKeyResultByObjectiveId(id);
        objectiveRepository.deleteById(id);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getAllObjective() {
        List<Objective> objectives = objectiveRepository.findAll();
        List<ObjectiveResponse> objectiveResponses = new ArrayList<>();

        objectives.forEach(objective -> {
            objectiveResponses.add(
                    ObjectiveResponse.builder()
                            .id(objective.getId())
                            .title(objective.getName())
                            .content(objective.getContent())
                            .userId(objective.getUser().getId())
                            .keyResults(keyResultService.getKeyResultsByObjectiveId(objective.getId()))
                            .build()
            );
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(objectiveResponses)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getObjectiveByObjectiveId(long id) {
        Objective objective = objectiveRepository.findById(id).orElse(null);
        if (objective == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }
        ObjectiveResponse objectiveResponse = ObjectiveResponse.builder()
                .id(objective.getId())
                .title(objective.getName())
                .content(objective.getContent())
                .userId(objective.getUser().getId())
                .keyResults(keyResultService.getKeyResultsByObjectiveId(objective.getId()))
                .build();

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(objectiveResponse)
                        .build()
        );
    }

    private boolean validateObjectiveInformation(ObjectvieDto objectvieDto) {
        return !objectvieDto.getTitle().trim().isEmpty() &&
                !objectvieDto.getContent().trim().isEmpty() &&
                objectvieDto.getUserId() != 0;
    }
}
