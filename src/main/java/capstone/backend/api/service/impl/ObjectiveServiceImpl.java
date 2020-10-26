package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.*;
import capstone.backend.api.entity.*;
import capstone.backend.api.entity.ApiResponse.Objective.ChildObjectiveResponse;
import capstone.backend.api.entity.ApiResponse.Objective.ObjectiveResponse;
import capstone.backend.api.entity.ApiResponse.Objective.ObjectiveTitleResponse;
import capstone.backend.api.repository.ObjectiveRepository;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.ObjectiveService;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Key;
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

    private JwtUtils jwtUtils;

    private UserRepository userRepository;

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
                    .parentId(objectvieDto.getParentId())
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
                    .parentId(objectvieDto.getParentId())
                    .execute(execute)
                    .status(objectvieDto.getStatus())
                    .type(objectvieDto.getType())
                    .weight(objectvieDto.getWeight())
                    .build();
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
                    KeyResultResponse keyResultResponse = setKeyResult(keyResult);
                    keyResultResponses.add(keyResultResponse);
                });
                logger.info("save key results successful");
            }
        }

        ObjectiveResponse objectiveResponse = setObjective(objective,keyResultResponses);

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
    public ResponseEntity<ApiResponse> getListChildObjectiveByObjectiveId(long objectiveId,long cycleId) throws Exception {
        Objective objectiveCurrent = objectiveRepository.findById(objectiveId).orElse(null);
        List<Objective> objectives = objectiveRepository.findAllByCycleIdAndParentId(cycleId,objectiveId);
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

    @Override
    public ResponseEntity<ApiResponse> getListObjectiveTitleByUserId(long userId) throws Exception {
        List<Objective> objectives = objectiveRepository.findAllByUserId(userId);

        List<MetaDataResponse> responses = new ArrayList<>();
        objectives.forEach(objective -> {
            responses.add(
                    MetaDataResponse.builder()
                    .id(objective.getId())
                    .name(objective.getName())
                    .build()
            );
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getParentObjectiveTitleByObjectiveId(long id,String token) throws Exception {
        ObjectiveTitleResponse response = new ObjectiveTitleResponse();
        List<MetaDataResponse> objectiveList = new ArrayList<>();
        Objective objective = objectiveRepository.findById(id).orElse(null);

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();


        if(id == 0){
            if (user.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_DIRECTOR"))) {
                response.setPosition("Director");
                response.setPermit(true);
            } else {
                response.setPosition("Staff");
                response.setPermit(false);
            }
            response.setObjectives(objectiveList);
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_SUCCESS())
                            .message(commonProperties.getMESSAGE_SUCCESS())
                            .data(response)
                            .build()
            );
        }

        if(objective == null){
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .data(response)
                            .build()
            );
        }

        Execute execute = objective.getExecute();
        if(execute.getProject() == null) {
            if (execute.getUser().getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_DIRECTOR"))) {
                response.setPosition("Director");
                response.setPermit(true);
            } else {
                response.setPosition("Staff");
                response.setPermit(false);
            }
        }else{
            List<Objective> objectives;
            long projectId = objective.getExecute().getProject().getId();
            Execute execute1 = executeService.getExecuteByUserIdAndProjectId(user.getId(),projectId);
            if(objective.getType() == 1){
                Objective parentObjective = objectiveRepository.findById(objective.getParentId()).get();
                long cycleId = parentObjective.getCycle().getId();
                if(objective.getExecute().getProject().getParentId().getId() == 0){
                    objectives = objectiveRepository.
                            findAllByCycleIdAndParentId(cycleId,0);
                    objectives.forEach(objective1 -> {
                        objectiveList.add(
                                MetaDataResponse.builder()
                                        .id(objective1.getId())
                                        .name(objective1.getName())
                                        .build()
                        );
                    });
                }else{
                    long parentProjectId = objective.getExecute().getProject().getParentId().getId();
                    objectives = objectiveRepository.
                            findAllByProjectIdAndCycleIdAndType(parentProjectId,cycleId,1);
                    objectives.forEach(objective1 -> {
                        objectiveList.add(
                                MetaDataResponse.builder()
                                        .id(objective1.getId())
                                        .name(objective1.getName())
                                        .build()
                        );
                    });
                }
            }else{
                long cycleId = objective.getCycle().getId();
                objectives = objectiveRepository.
                        findAllByProjectIdAndCycleIdAndType(projectId,cycleId,1);
                objectives.forEach(objective1 -> {
                    objectiveList.add(
                            MetaDataResponse.builder()
                                    .id(objective1.getId())
                                    .name(objective1.getName())
                                    .build()
                    );
                });
            }
            if(execute1 == null){
                response.setPosition("None");
                response.setPermit(false);
            }else{
                if(objective.getType() == 1){
                    String position = execute1.getPosition().getName();
                    response.setPosition(position);
                    response.setPermit(execute1.isPm());
                }else if(objective.getType() == 2){
                    String position = execute1.getPosition().getName();
                    response.setPosition(position);
                    response.setPermit(true);
                }
            }
        }
        response.setObjectives(objectiveList);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getParentKeyResultTitleByObjectiveId(long id) throws Exception {
        ArrayList<MetaDataResponse> responses = new ArrayList<>();
        if(id == 0){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_SUCCESS())
                            .message(commonProperties.getMESSAGE_SUCCESS())
                            .data(responses)
                            .build()
            );
        }
        Objective objective = objectiveRepository.findById(id).orElse(null);

        if(objective == null){
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );
        }
        Objective parentObjective = objectiveRepository.findById(objective.getParentId()).orElse(null);
        if(parentObjective != null){
            ArrayList<KeyResult> keyResults =  keyResultService.getKeyResultsByObjectiveId(parentObjective.getId());

            keyResults.forEach(keyResult -> {
                responses.add(
                        MetaDataResponse.builder()
                                .id(keyResult.getId())
                                .name(keyResult.getContent())
                                .build()
                );
            });
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses)
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getListAlignByObjectiveId(long id) throws Exception {
        List<MetaDataResponse> responses = new ArrayList<>();
        List<Objective> objectives;

        if(id == 0){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_SUCCESS())
                            .message(commonProperties.getMESSAGE_SUCCESS())
                            .data(responses)
                            .build()
            );
        }

        Objective objective = objectiveRepository.findById(id).orElse(null);
        if(objective == null){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );
        }

        Execute execute = objective.getExecute();
        Project project = execute.getProject();
        Cycle cycle = objective.getCycle();
        int type = objective.getType();

        if(type == 0){
             objectives = objectiveRepository.findAllByTypeAndCycleId(type,cycle.getId());
        } else {
            objectives = objectiveRepository.findAllByProjectIdAndCycleIdAndType(project.getId(),cycle.getId(),type);
        }

        objectives.forEach(obj ->{
            responses.add(
                    MetaDataResponse.builder()
                            .id(obj.getId())
                            .name(obj.getName())
                            .build()
            );
        });

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses)
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
            String[] array = string.split(",");
            for (String item: array) {
                if(!item.trim().isEmpty()){
                    longArray.add(Long.parseLong(item.trim()));
                }
            }
        }
        return longArray;
    }

    private String arrayToString(List<Long> longArray){
        StringBuilder string = new StringBuilder(",");
        for (Long along: longArray) {
            string.append(along).append(",");
        }
        return string.toString();
    }

    public ObjectiveResponse setObjective(Objective objective, List<KeyResultResponse> keyResults){
        return ObjectiveResponse.builder().id(objective.getId())
                .title(objective.getName())
                .content(objective.getContent())
                .userId(objective.getExecute().getUser().getId())
                .projectId(objective.getExecute().getProject() == null ? 0 :
                        objective.getExecute().getProject().getId())
                .alignmentObjectives(stringToArray(objective.getAlignmentObjectives()))
                .changing(objective.getChanging())
                .cycleId(objective.getCycle().getId())
                .parentId(objective.getParentId())
                .progress(objective.getProgress())
                .status(objective.getStatus())
                .type(objective.getType())
                .weight(objective.getWeight())
                .keyResults(keyResults)
                .build();
    }

    public KeyResultResponse setKeyResult(KeyResult keyResult){
        return KeyResultResponse.builder()
                .id(keyResult.getId())
                .content(keyResult.getContent())
                .measureUnitId(keyResult.getUnitOfKeyResult().getId())
                .startValue(keyResult.getFromValue())
                .targetedValue(keyResult.getToValue())
                .valueObtained(keyResult.getValueObtained())
                .reference(keyResult.getReference())
                .build();
    }
}
