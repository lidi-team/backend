package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.*;
import capstone.backend.api.entity.*;
import capstone.backend.api.repository.ObjectiveRepository;
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

    private ObjectiveRepository objectiveRepository;

    private KeyResultServiceImpl keyResultService;

    private ExecuteServiceImpl executeService;

    private CycleServiceImpl cycleService;

    @Override
    public ResponseEntity<ApiResponse> addObjective(ObjectvieDto objectvieDto) throws Exception {
        if (!validateObjectiveInformation(objectvieDto)) {
            logger.error("Parameter is empty!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }

        Execute execute = executeService.getExecuteByUserIdAndProjectId(objectvieDto.getUserId(),objectvieDto.getProjectId());
        String alignmentObjectives = arrayToString(objectvieDto.getAlignmentObjectives());
        String parentObjectives = arrayToString(objectvieDto.getParentId());
        Cycle cycle = cycleService.getCycleById(objectvieDto.getCycleId());

        Objective objective;
        if (objectvieDto.getId() == 0) {
            objective = Objective.builder()
                    .name(objectvieDto.getTitle())
                    .content(objectvieDto.getContent())
                    .cycle(cycle)
                    .progress(objectvieDto.getProgress())
                    .changing(objectvieDto.getChanging())
                    .alignmentObjectives(alignmentObjectives)
                    .parentId(parentObjectives)
                    .execute(execute)
                    .status(objectvieDto.getStatus())
                    .type(objectvieDto.getType())
                    .weight(objectvieDto.getWeight())
                    .build();
        } else {
            objective = Objective.builder()
                    .id(objectvieDto.getId())
                    .name(objectvieDto.getTitle())
                    .content(objectvieDto.getContent())
                    .cycle(cycle)
                    .progress(objectvieDto.getProgress())
                    .changing(objectvieDto.getChanging())
                    .alignmentObjectives(alignmentObjectives)
                    .parentId(parentObjectives)
                    .execute(execute)
                    .status(objectvieDto.getStatus())
                    .type(objectvieDto.getType())
                    .weight(objectvieDto.getWeight())
                    .build();
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
                            //.keyResults(keyResultService.getKeyResultsByObjectiveId(objective.getId()))
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
                //.keyResults(keyResultService.getKeyResultsByObjectiveId(objective.getId()))
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
    public ResponseEntity<ApiResponse> getListChildObjectiveByObjectiveId(long objectiveId,long cycleId) throws Exception {
        Objective objectiveCurrent = objectiveRepository.findById(objectiveId).orElse(null);
        List<Objective> objectives = objectiveRepository.findAllByCycleIdAndParentIdContains(cycleId," " + objectiveId + " ");
        List<ChildObjectiveResponse> childObjectiveResponses = new ArrayList<>();
        ChildObjectiveResponse childObject = new ChildObjectiveResponse();
        objectives.forEach(objective -> {
            User user = objective.getExecute().getUser();
            Project project = objective.getExecute().getProject();
            ArrayList<KeyResult> keyResults = keyResultService.getKeyResultsByObjectiveId(objective.getId());
            childObjectiveResponses.add(
                    ChildObjectiveResponse.builder()
                            .id(objective.getId())
                            .title(objective.getName())
                            .progress(objective.getProgress())
                            .changing(objective.getChanging())
                            .cycleId(objective.getCycle().getId())
                            .parentObjectiveId(objective.getParentId())
                            .keyResults(childObject.keyResultOfChildObjectives(keyResults))
                            .alignmentObjectives(stringToArray(objective.getAlignmentObjectives()))
                            .author(childObject.authorOfChildObjective(user))
                            .type(childObject.setObjectiveType(
                                    objective.getType(),
                                    project == null ? " " : project.getName()))
                            .build()
            );

        });

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(
                                DrillDownObjectiveResponse.builder()
                                .childObjectives(childObjectiveResponses)
                                .title(objectiveCurrent == null ? "Objective của công ty" : objectiveCurrent.getName())
                                .build()
                        )
                        .build()
        );
    }

    private boolean validateObjectiveInformation(ObjectvieDto objectvieDto) {
        return !objectvieDto.getTitle().trim().isEmpty() &&
                !objectvieDto.getContent().trim().isEmpty() &&
                objectvieDto.getUserId() != 0;
    }

    private ArrayList<Long> stringToArray(String string){
        ArrayList<Long> longArray = new ArrayList<>();
        if(string != null && !string.trim().isEmpty()){
            String[] array = string.split(" ");
            for (String item: array) {
                if(!item.trim().isEmpty()){
                    longArray.add(Long.parseLong(item.trim()));
                }
            }
        }
        return longArray;
    }

    private String arrayToString(List<Long> longArray){
        StringBuilder string = new StringBuilder(" ");
        for (Long along: longArray) {
            string.append(along).append(" ");
        }
        return string.toString();
    }
}
