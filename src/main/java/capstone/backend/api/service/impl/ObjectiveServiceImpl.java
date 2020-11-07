package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.*;
import capstone.backend.api.entity.ApiResponse.Objective.*;
import capstone.backend.api.entity.*;
import capstone.backend.api.repository.ObjectiveRepository;
import capstone.backend.api.repository.UserRepository;
import capstone.backend.api.service.ObjectiveService;
import capstone.backend.api.utils.CommonUtils;
import capstone.backend.api.utils.security.JwtUtils;
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

    private final CommonProperties commonProperties;

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveServiceImpl.class);

    private final ObjectiveRepository objectiveRepository;

    private final KeyResultServiceImpl keyResultService;

    private final ExecuteServiceImpl executeService;

    private final CycleServiceImpl cycleService;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private  final CommonUtils commonUtils;

    @Override
    public ResponseEntity<ApiResponse> addObjective(ObjectvieDto objectvieDto, String token) throws Exception {
        if (!validateObjectiveInformation(objectvieDto)) {
            logger.error("Parameter is empty!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_EMPTY())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        Execute execute = executeService.getExecuteByUserIdAndProjectId(user.getId(), objectvieDto.getProjectId());
        String alignmentObjectives = arrayToString(objectvieDto.getAlignmentObjectives());
        Cycle cycle = cycleService.getCycleById(objectvieDto.getCycleId());

        Objective objective;
        if (objectvieDto.getId() == 0) {
            objective = Objective.builder()
                    .name(objectvieDto.getTitle())
                    .cycle(cycle)
                    .alignmentObjectives(alignmentObjectives)
                    .parentId(objectvieDto.getParentId())
                    .execute(execute)
                    .status(commonProperties.getOBJ_RUNNING())
                    .type(objectvieDto.getType())
                    .weight(objectvieDto.getWeight())
                    .build();
        } else {
            objective = Objective.builder()
                    .id(objectvieDto.getId())
                    .name(objectvieDto.getTitle())
                    .cycle(cycle)
                    .alignmentObjectives(alignmentObjectives)
                    .parentId(objectvieDto.getParentId())
                    .execute(execute)
                    .status(commonProperties.getOBJ_RUNNING())
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

        ObjectiveResponse objectiveResponse = setObjective(objective, keyResultResponses);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(objectiveResponse).build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> deleteObjective(long id) {

        Objective objective = objectiveRepository.findByIdAndDelete(id);
        if (objective == null) {
            logger.error("Objective not found!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        if ((objective.getType() == commonProperties.getOBJ_PROJECT() ||
                objective.getType() == commonProperties.getOBJ_COMPANY())
                && objectiveRepository.findFirstByParentId(objective.getId()) != null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_INVALID())
                            .message("Không thể xóa mục tiêu của công ty/dự án khi nó đã có mục tiêu con").build()
            );
        }

        keyResultService.deleteKeyResultByObjectiveId(id);
        objectiveRepository.deleteObjective(id);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponse> getListChildObjectiveByObjectiveId(long objectiveId, long cycleId) throws Exception {
        Objective objectiveCurrent = objectiveRepository.findByIdAndDelete(objectiveId);
        List<Objective> objectives = objectiveRepository.findAllByCycleIdAndParentId(cycleId, objectiveId);
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
    public ResponseEntity<ApiResponse> getParentObjectiveTitleByObjectiveId(long id, String token) throws Exception {
        ObjectiveTitleResponse response = new ObjectiveTitleResponse();
        List<MetaDataResponse> objectiveList = new ArrayList<>();
        Objective objective = objectiveRepository.findByIdAndDelete(id);

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();


        if (id == 0) {
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

        if (objective == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .data(response)
                            .build()
            );
        }

        Execute execute = objective.getExecute();
        if (execute.getProject() == null) {
            if (execute.getUser().getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_DIRECTOR"))) {
                response.setPosition("Director");
                response.setPermit(true);
            } else {
                response.setPosition("Staff");
                response.setPermit(false);
            }
        } else {
            List<Objective> objectives;
            long projectId = objective.getExecute().getProject().getId();
            Execute execute1 = executeService.getExecuteByUserIdAndProjectId(user.getId(), projectId);
            if (objective.getType() == 1) {
                Objective parentObjective = objectiveRepository.findByIdAndDelete(objective.getParentId());
                long cycleId = parentObjective.getCycle().getId();
                if (objective.getExecute().getProject().getParent() == null) {
                    objectives = objectiveRepository.
                            findAllByCycleIdAndParentId(cycleId, 0);
                    objectives.forEach(objective1 -> {
                        objectiveList.add(
                                MetaDataResponse.builder()
                                        .id(objective1.getId())
                                        .name(objective1.getName())
                                        .build()
                        );
                    });
                } else {
                    long parentProjectId = objective.getExecute().getProject().getParent().getId();
                    objectives = objectiveRepository.
                            findAllByProjectIdAndCycleIdAndType(parentProjectId, cycleId, 1);
                    objectives.forEach(objective1 -> {
                        objectiveList.add(
                                MetaDataResponse.builder()
                                        .id(objective1.getId())
                                        .name(objective1.getName())
                                        .build()
                        );
                    });
                }
            } else {
                long cycleId = objective.getCycle().getId();
                objectives = objectiveRepository.
                        findAllByProjectIdAndCycleIdAndType(projectId, cycleId, 1);
                objectives.forEach(objective1 -> {
                    objectiveList.add(
                            MetaDataResponse.builder()
                                    .id(objective1.getId())
                                    .name(objective1.getName())
                                    .build()
                    );
                });
            }
            if (execute1 == null) {
                response.setPosition("None");
                response.setPermit(false);
            } else {
                if (objective.getType() == 1) {
                    String position = execute1.getPosition().getName();
                    response.setPosition(position);
                    response.setPermit(execute1.isPm());
                } else if (objective.getType() == 2) {
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
        if (id == 0) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_SUCCESS())
                            .message(commonProperties.getMESSAGE_SUCCESS())
                            .data(responses)
                            .build()
            );
        }
        Objective objective = objectiveRepository.findByIdAndDelete(id);

        if (objective == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );
        }
        Objective parentObjective = objectiveRepository.findByIdAndDelete(objective.getParentId());
        if (parentObjective != null) {
            ArrayList<KeyResult> keyResults = keyResultService.getKeyResultsByObjectiveId(parentObjective.getId());

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

        if (id == 0) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_SUCCESS())
                            .message(commonProperties.getMESSAGE_SUCCESS())
                            .data(responses)
                            .build()
            );
        }

        Objective objective = objectiveRepository.findByIdAndDelete(id);
        if (objective == null) {
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

        if (type == 0) {
            objectives = objectiveRepository.findAllByTypeAndCycleId(type, cycle.getId());
        } else {
            objectives = objectiveRepository.findAllByProjectIdAndCycleIdAndType(project.getId(), cycle.getId(), type);
        }

        objectives.forEach(obj -> {
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

    @Override
    public ResponseEntity<ApiResponse> getKeyResultTitleByObjectiveId(long id) throws Exception {
        ArrayList<MetaDataResponse> responses = new ArrayList<>();
        Objective objective = objectiveRepository.findByIdAndDelete(id);

        if (objective == null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );
        }
        ArrayList<KeyResult> keyResults = keyResultService.getKeyResultsByObjectiveId(objective.getId());

        keyResults.forEach(keyResult -> {
            responses.add(
                    MetaDataResponse.builder()
                            .id(keyResult.getId())
                            .name(keyResult.getContent())
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
    public ResponseEntity<?> getAllObjectiveAndProjectOfUser(String token, long cycleId) throws Exception {
        List<ProjectOfUserResponse> responses = new ArrayList<>();
        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        List<Execute> executes = executeService.getListExecuteByUserId(user.getId());

        for (Execute execute : executes) {
            if (execute.getProject() != null) {
                List<ObjectiveProjectItem> objectiveItems = new ArrayList<>();
                List<Objective> objectives = objectiveRepository
                        .findAllByProjectIdAndCycleIdAndType(execute.getProject().getId(), cycleId, commonProperties.getOBJ_PROJECT());
                objectives.forEach(objective -> {
                    //get list key result objective
                    List<KeyResultResponse> keyResultResponses = setListKeyResultResponse(objective);

                    //get list child objective of user in project
                    List<ObjectiveProjectItem> childItems = new ArrayList<>();
                    List<Objective> childObjectives = objectiveRepository
                            .findAllByParentIdAndUserIdAndType(objective.getId(), user.getId(), commonProperties.getOBJ_PERSONAL());
                    childObjectives.forEach(childObjective -> {
                        //get list key result of child objective
                        List<KeyResultResponse> childKeyResultResponses = setListKeyResultResponse(childObjective);

                        childItems.add(
                                ObjectiveProjectItem.builder()
                                        .id(childObjective.getId())
                                        .title(childObjective.getName())
                                        .type(childObjective.getType())
                                        .weight(childObjective.getWeight())
                                        .progress(childObjective.getProgress())
                                        .changing(childObjective.getChanging())
                                        .keyResults(childKeyResultResponses)
                                        .childObjectives(new ArrayList<>())
                                        .build()
                        );
                    });

                    objectiveItems.add(
                            ObjectiveProjectItem.builder()
                                    .id(objective.getId())
                                    .title(objective.getName())
                                    .type(objective.getType())
                                    .weight(objective.getWeight())
                                    .progress(objective.getProgress())
                                    .changing(objective.getChanging())
                                    .keyResults(keyResultResponses)
                                    .childObjectives(childItems)
                                    .build()
                    );

                });
                responses.add(
                        ProjectOfUserResponse.builder()
                                .id(execute.getProject().getId())
                                .name(execute.getProject().getName())
                                .position(execute.getPosition().getName())
                                .objectives(objectiveItems)
                                .isPm(execute.isPm())
                                .build()
                );
            }
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses)
                        .build()
        );
    }

    private boolean validateObjectiveInformation(ObjectvieDto objectvieDto) {
        return !objectvieDto.getTitle().trim().isEmpty();
    }

    private ArrayList<Long> stringToArray(String string) {
        ArrayList<Long> longArray = new ArrayList<>();
        if (string != null && !string.trim().isEmpty()) {
            String[] array = string.split(",");
            for (String item : array) {
                if (!item.trim().isEmpty()) {
                    longArray.add(Long.parseLong(item.trim()));
                }
            }
        }
        return longArray;
    }

    private String arrayToString(List<Long> longArray) {
        StringBuilder string = new StringBuilder(",");
        for (Long along : longArray) {
            string.append(along).append(",");
        }
        return string.toString();
    }

    public ObjectiveResponse setObjective(Objective objective, List<KeyResultResponse> keyResults) {
        return ObjectiveResponse.builder().id(objective.getId())
                .title(objective.getName())
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

    public KeyResultResponse setKeyResult(KeyResult keyResult) {
        return KeyResultResponse.builder()
                .id(keyResult.getId())
                .content(keyResult.getContent())
                .measureUnitId(keyResult.getUnitOfKeyResult().getId())
                .startValue(keyResult.getFromValue())
                .targetedValue(keyResult.getToValue())
                .valueObtained(keyResult.getValueObtained())
                .progress(commonUtils.calculateProgressKeyResult(keyResult))
                .reference(keyResult.getReference())
                .build();
    }

    public List<KeyResultResponse> setListKeyResultResponse(Objective objective) {
        List<KeyResultResponse> keyResultResponses = new ArrayList<>();
        //get list key result of project objective
        List<KeyResult> keyResults = keyResultService.getKeyResultsByObjectiveId(objective.getId());
        keyResults.forEach(keyResult -> {
            KeyResultResponse response = setKeyResult(keyResult);
            keyResultResponses.add(response);
        });

        return keyResultResponses;
    }
}
