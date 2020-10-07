package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.CycleResponse;
import capstone.backend.api.entity.ApiResponse.KeyResultResponse;
import capstone.backend.api.entity.ApiResponse.ObjectiveResponse;
import capstone.backend.api.entity.Cycle;
import capstone.backend.api.entity.KeyResult;
import capstone.backend.api.entity.Objective;
import capstone.backend.api.entity.User;
import capstone.backend.api.repository.CycleRepository;
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

    private CycleRepository cycleRepository;

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
        Objective parentObjective = null;
        if(objectvieDto.getParentObjectiveId() != 0){
            parentObjective = objectiveRepository.findById(objectvieDto.getParentObjectiveId()).get();
        }
        Cycle cycle = cycleRepository.findById(objectvieDto.getCycleId()).get();
        Objective objective;
        if (objectvieDto.getId() == 0) {
            objective = Objective.builder()
                    .title(objectvieDto.getTitle())
                    .content(objectvieDto.getContent())
                    .progress(objectvieDto.getProgress())
                    .isRootObjective(objectvieDto.isRootObjective())
                    .parentObjective(parentObjective)
                    .cycle(cycle)
                    .user(user).build();
        } else {
            objective = Objective.builder()
                    .id(objectvieDto.getId())
                    .title(objectvieDto.getTitle())
                    .content(objectvieDto.getContent())
                    .progress(objectvieDto.getProgress())
                    .isRootObjective(objectvieDto.isRootObjective())
                    .parentObjective(parentObjective)
                    .cycle(cycle)
                    .user(user).build();
        }

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
                objective = objectiveRepository.save(objective);
                logger.info("save objective successful");

                ArrayList<KeyResult> keyResults =
                        keyResultService.addKeyResults(objectvieDto.getKeyResults(), objective);
                keyResults.forEach(keyResult -> {
                    keyResultResponses.add(new KeyResultResponse()
                            .builder()
                            .id(keyResult.getId())
                            .content(keyResult.getContent())
                            .startValue(keyResult.getStartValue())
                            .valueObtained(keyResult.getValueObtained())
                            .targetedValue(keyResult.getTargetedValue())
                            .measureUnitId(keyResult.getMeasureUnit().getId())
                            .common(keyResult.getCommon())
                            .build());
                });
                logger.info("save key results successful");
            }
        }

        ObjectiveResponse objectiveResponse = ObjectiveResponse.builder().id(objective.getId())
                .progress(objective.getProgress())
                .title(objective.getTitle())
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

        Objective objective = objectiveRepository.getOne(id);
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
    public ResponseEntity<ApiResponse> getAllObjective(long cycleId) {
        List<Objective> objectives = objectiveRepository.findAllByCycleId(cycleId);
        List<ObjectiveResponse> objectiveResponses = new ArrayList<>();

        for (Objective objective: objectives){
            objectiveResponses.add(
                    new ObjectiveResponse().builder()
                            .id(objective.getId())
                            .title(objective.getTitle())
                            .content(objective.getContent())
                            .progress(objective.getProgress())
                            .isRootObjective(objective.isRootObjective())
                            .parentObjectiveId(objective.getParentObjective() == null ? 0 : objective.getParentObjective().getId())
                            .cycle(CycleResponse.builder()
                                    .id(objective.getCycle().getId())
                                    .name(objective.getCycle().getName())
                                    .startDate(objective.getCycle().getStartDate())
                                    .endDate(objective.getCycle().getEndDate())
                                    .build())
                            .keyResults(keyResultService.getKeyResultsByObjectiveId(objective.getId()))
                            .userId(objective.getUser().getId()).build()
            );
        };
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
        Objective objective = objectiveRepository.getOne(id);
        if (objective == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }
        ObjectiveResponse objectiveResponse = new ObjectiveResponse().builder()
                .id(objective.getId())
                .title(objective.getTitle())
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

    @Override
    public ResponseEntity<ApiResponse> getObjectiveByUserId(long id) {
        User user = userRepository.findById(id).orElse(null);

        if(user == null){
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        List<Objective> objectives = objectiveRepository.findAllByUserId(id);
        List<ObjectiveResponse> objectiveResponses = new ArrayList<>();
        for(Objective objective: objectives){
            Cycle cycle = cycleRepository.findById(objective.getCycle().getId()).get();
            convertObjectives2ObjectiveResponses(user, cycle, objectiveResponses, objective);
        };

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(objectiveResponses)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> searchByCycleIdAndUserId(long cycleId, long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Cycle cycle = cycleRepository.findById(cycleId).orElse(null);

        if(user == null || cycle == null){
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }
        List<Objective> objectives = objectiveRepository.findAllByCycleIdAndUserId(cycleId,userId);
        List<ObjectiveResponse> objectiveResponses = new ArrayList<>();
        objectives.forEach(objective -> {
            convertObjectives2ObjectiveResponses(user, cycle, objectiveResponses, objective);
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
    public ResponseEntity<ApiResponse> viewListObjectiveByCycleId(long cycleId) {
        Cycle cycle = cycleRepository.findById(cycleId).orElse(null);

        if(cycle == null){
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        List<Objective> objectives = objectiveRepository.findAllByCycleId(cycleId);
        List<ObjectiveResponse> objectiveResponses = new ArrayList<>();
        objectives.forEach(objective -> {
            User user = userRepository.findById(objective.getUser().getId()).get();
            convertObjectives2ObjectiveResponses(user, cycle, objectiveResponses, objective);
        });

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(objectiveResponses)
                        .build()
        );
    }


    private boolean validateObjectiveInformation(ObjectvieDto objectvieDto) {
        if (objectvieDto.getTitle().trim().isEmpty() ||
                objectvieDto.getContent().trim().isEmpty() ||
                objectvieDto.getUserId() == 0) {
            return false;
        }
        return true;
    }

    private void convertObjectives2ObjectiveResponses(User user, Cycle cycle, List<ObjectiveResponse> objectiveResponses, Objective objective) {
        ArrayList<KeyResultResponse> keyResults = keyResultService.getKeyResultsByObjectiveId(objective.getId());
        objectiveResponses.add(
                new ObjectiveResponse().builder()
                        .id(objective.getId())
                        .title(objective.getTitle())
                        .content(objective.getContent())
                        .progress(objective.getProgress())
                        .isRootObjective(objective.isRootObjective())
                        .parentObjectiveId(objective.getParentObjective() == null ? 0 : objective.getParentObjective().getId())
                        .cycle(CycleResponse.builder()
                                .id(objective.getCycle().getId())
                                .name(objective.getCycle().getName())
                                .startDate(objective.getCycle().getStartDate())
                                .endDate(objective.getCycle().getEndDate())
                                .build())
                        .keyResults(keyResults)
                        .userId(user.getId()).build()
        );
    }
}
