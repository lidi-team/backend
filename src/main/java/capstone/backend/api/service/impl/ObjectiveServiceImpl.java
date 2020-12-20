package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.KeyResultProgress;
import capstone.backend.api.dto.ObjectvieDto;
import capstone.backend.api.entity.ApiResponse.*;
import capstone.backend.api.entity.ApiResponse.KeyResult.KeyResultResponse;
import capstone.backend.api.entity.ApiResponse.Objective.*;
import capstone.backend.api.entity.*;
import capstone.backend.api.repository.*;
import capstone.backend.api.service.ObjectiveService;
import capstone.backend.api.utils.CommonUtils;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    private final ReportRepository reportRepository;

    private final ExecuteRepository executeRepository;

    private final ProjectRepository projectRepository;

    @Override
    public ResponseEntity<?> addObjective(ObjectvieDto objectvieDto, String token) throws Exception {
        if (!validateObjectiveInformation(objectvieDto)) {
            logger.error("Parameter is empty!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_EMPTY()).build()
            );
        }

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        Execute execute;
        if(objectvieDto.getType() == 0){
            execute = executeService.findDirectorExecute();

        } else {
            execute = executeService.getExecuteByUserIdAndProjectId(user.getId(), objectvieDto.getProjectId());
        }

        String alignmentObjectives = commonUtils.arrayToString(objectvieDto.getAlignmentObjectives());
        Cycle cycle = cycleService.getCycleById(objectvieDto.getCycleId());

        Objective objective;
        if (objectvieDto.getId() == 0) {
            objective = Objective.builder()
                    .name(objectvieDto.getTitle())
                    .cycle(cycle)
                    .createAt(new Date())
                    .alignmentObjectives(alignmentObjectives)
                    .parentId(objectvieDto.getParentId())
                    .execute(execute)
                    .status(commonProperties.getOBJ_RUNNING())
                    .type(objectvieDto.getType())
                    .weight(objectvieDto.getWeight())
                    .build();
        } else {
            objective = objectiveRepository.findById(objectvieDto.getId()).get();
            objective = Objective.builder()
                    .id(objectvieDto.getId())
                    .name(objectvieDto.getTitle())
                    .cycle(cycle)
                    .createAt(objective.getCreateAt())
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
                                .code(commonProperties.getCODE_UPDATE_FAILED())
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

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message("Tạo mới/Cập nhật mục tiêu thành công!")
                        .data(objectiveResponse).build()
        );
    }

    @Override
    public ResponseEntity<?> deleteObjective(long id) {

        Objective objective = objectiveRepository.findByIdAndDelete(id);
        if (objective == null) {
            logger.error("Objective not found!");
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        if ((objective.getType() == commonProperties.getOBJ_PROJECT() ||
                objective.getType() == commonProperties.getOBJ_COMPANY())
                && objectiveRepository.findFirstByParentId(objective.getId()) != null) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UPDATE_FAILED())
                            .message("Không thể xóa mục tiêu của công ty/dự án khi nó đã có mục tiêu con").build()
            );
        }

        keyResultService.deleteKeyResultByObjectiveId(id);
        objectiveRepository.deleteObjective(id);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getListChildObjectiveByObjectiveId(long objectiveId, long cycleId) throws Exception {
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
                            .alignmentObjectives(commonUtils.stringToArray(objective.getAlignmentObjectives()))
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
    public ResponseEntity<?> getListObjectiveTitleByUserId(long userId) throws Exception {
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
    public ResponseEntity<?> getParentObjectiveTitleByObjectiveId(long id, String token) throws Exception {
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
    public ResponseEntity<?> getParentKeyResultTitleByObjectiveId(long id) throws Exception {
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
    public ResponseEntity<?> getListAlignByProjectIdAndCycleId(long projectId,long cycleId) throws Exception {
        List<AlignObjectiveResponse> responses = new ArrayList<>();

        List<Objective> objectives = objectiveRepository.findAllObjectiveByProjectIdAndCycleId(projectId,cycleId);
        objectives.forEach(obj -> {
            responses.add(
                    AlignObjectiveResponse.builder()
                            .id(obj.getId())
                            .name(obj.getName())
                            .type(obj.getType())
                            .user(obj.getExecute().getUser().getFullName())
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
    public ResponseEntity<?> getKeyResultTitleByObjectiveId(long id) throws Exception {
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
        Cycle cycle = cycleService.getCycleById(cycleId);

        List<Execute> executeOrigins = executeRepository.findAllByUserId(user.getId());

        List<Execute> executes = executeOrigins.stream().filter(execute ->
                !(execute.getEndDate().before(cycle.getFromDate())
                        || execute.getFromDate().after(cycle.getEndDate()))).collect(Collectors.toList());

        List<Long> projectIds = executes.stream().filter(execute -> execute.getProject() != null)
                .map(execute -> execute.getProject().getId()).collect(Collectors.toList());

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
                                        .delete(checkDeleteAndUpdateObjective(childObjective))
                                        .update(checkDeleteAndUpdateObjective(childObjective))
                                        .alignObjectives(setListAlign(childObjective.getAlignmentObjectives()))
                                        .keyResults(childKeyResultResponses)
                                        .parentId(childObjective.getParentId())
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
                                    .update(checkUpdateForParentObjective(childItems,objective))
                                    .changing(objective.getChanging())
                                    .keyResults(keyResultResponses)
                                    .alignObjectives(setListAlign(objective.getAlignmentObjectives()))
                                    .childObjectives(childItems)
                                    .parentId(objective.getParentId())
                                    .delete(childItems.size() == 0)
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

    @Override
    public ResponseEntity<?> getDetailObjectiveById(long id) throws Exception {
        Objective objective = objectiveRepository.findById(id).get();
        List<MetaDataResponse> childrenResponses = new ArrayList<>();
        List<MetaDataResponse> alignResponses = new ArrayList<>();

        Objective parent = objectiveRepository.findByIdAndDelete(objective.getParentId());
        MetaDataResponse parentResponse = null;
        if(parent != null){
            parentResponse = MetaDataResponse.builder()
                    .id(parent.getId())
                    .name(parent.getName())
                    .build();
        }


        List<Objective> children = objectiveRepository.findAllByParentId(objective.getId());
        children.forEach(child ->{
            childrenResponses.add(
                    MetaDataResponse.builder()
                            .id(child.getId())
                            .name(child.getName())
                            .build()
            );
        });

        List<Long> aligns = commonUtils.stringToArray(objective.getAlignmentObjectives());
        aligns.forEach(item ->{
            Objective align = objectiveRepository.findByIdAndDelete(item);
            if(align != null){
                alignResponses.add(
                        MetaDataResponse.builder()
                                .id(align.getId())
                                .name(align.getName())
                                .build()
                );
            }
        });

        List<KeyResultResponse> resultResponses = setListKeyResultResponse(objective);

        User user = objective.getExecute().getUser();
        MetaDataResponse userResponse = MetaDataResponse.builder()
                .id(user.getId())
                .name(user.getFullName())
                .build();

        Project project = objective.getExecute().getProject();
        MetaDataResponse projectResponse = null;
        if(project != null){
            projectResponse = MetaDataResponse.builder()
                    .id(project.getId())
                    .name(project.getName())
                    .build();
        }

        ObjectiveDetailResponse response = ObjectiveDetailResponse.builder()
                .id(objective.getId())
                .title(objective.getName())
                .progress(objective.getProgress())
                .weight(objective.getWeight())
                .parentObjective(parentResponse)
                .user(userResponse)
                .project(projectResponse)
                .childObjectives(childrenResponses)
                .alignmentObjectives(alignResponses)
                .keyResults(resultResponses)
                .build();

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getAlignObjectiveById(long id) throws Exception {
        List<ObjectiveProjectItem> responses = new ArrayList<>();
        Objective objective = objectiveRepository.findById(id).get();

        List<Long> longs = commonUtils.stringToArray(objective.getAlignmentObjectives());
        longs.forEach(item ->{
            Objective align = objectiveRepository.findByIdAndDelete(item);
            if(align != null){
                List<KeyResultResponse> keyResultResponses = setListKeyResultResponse(align);
                responses.add(
                        ObjectiveProjectItem.builder()
                                .id(align.getId())
                                .title(align.getName())
                                .type(align.getType())
                                .weight(align.getWeight())
                                .progress(align.getProgress())
                                .changing(align.getChanging())
                                .keyResults(keyResultResponses)
                                .childObjectives(new ArrayList<>())
                                .delete(false)
                                .build()
                );
            }
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
    public ResponseEntity<?> getListAlignProjectByProjectIdAndCycleId(long projectId, long cycleId) throws Exception {
        List<MetaDataResponse> responses = new ArrayList<>();

        if(projectId == 0){
            List<Objective> objectives = objectiveRepository.findCompanyObjective(cycleId);
            objectives.forEach(obj -> {
                responses.add(
                        MetaDataResponse.builder()
                                .id(obj.getId())
                                .name(obj.getName())
                                .build()
                );
            });
        }else{

            List<Objective> objectives = objectiveRepository.findAllProjectObjective(projectId,cycleId);
            objectives.forEach(obj -> {
                responses.add(
                        MetaDataResponse.builder()
                                .id(obj.getId())
                                .name(obj.getName())
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
    public ResponseEntity<?> getListMetaDataOfUser(long userId) throws Exception {

        List<Objective> objectives = objectiveRepository.findAllByUserId(userId);

        for (int i = 0; i < objectives.size(); i++) {
            if(reportRepository.findAllByObjectiveId(objectives.get(i).getId()).size() == 0){
                objectives.remove(objectives.get(i));
                i--;
            }

        }

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
    public ResponseEntity<?> getProgressOKR(long cycleId, String token) throws Exception {
        Map<String,Object> response = new HashMap<>();

        Cycle cycle = cycleService.getCycleById(cycleId);
        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        List<Execute> executeOrigins = executeRepository.findAllByUserId(user.getId());
        List<Execute> executes = executeOrigins.stream().filter(execute ->
                !(execute.getEndDate().before(cycle.getFromDate())
                        || execute.getFromDate().after(cycle.getEndDate()))).collect(Collectors.toList());


        List<Map<String,Object>> projectList = new ArrayList<>();
        for (Execute execute : executes) {
            if(execute.getProject() != null){
                List<Objective> projectOKRs = objectiveRepository.findAllByProjectIdAndCycleIdAndType(execute.getProject().getId(),cycleId,1);
                List<Objective> personalOKRs = objectiveRepository.findAllByExecuteIdAndCycleIdAndType(execute.getId(),cycleId,2);

                double projectProgress = calculateProgress(projectOKRs);
                double personalProgress = calculateProgress(personalOKRs);

                Map<String,Object> project = new HashMap<>();

                project.put("id",execute.getProject().getId());
                project.put("name",execute.getProject().getName());
                project.put("projectProgress",projectProgress);
                project.put("personalProgress",personalProgress);

                projectList.add(project);
            }
        }

        List<Objective> companyObjectives = objectiveRepository.findCompanyObjective(cycleId);
        double companyProgress = calculateProgress(companyObjectives);

        response.put("projects",projectList);
        response.put("company",companyProgress);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    private double calculateProgress(List<Objective> OKRs) {
        double weight = 0;
        double totalProgress = 0;

        for (Objective okr : OKRs) {
            totalProgress += okr.getProgress()*okr.getWeight();
            weight += okr.getWeight();
        }
        if(weight == 0){
            weight = 1;
        }
        return totalProgress/weight;
    }

    private boolean validateObjectiveInformation(ObjectvieDto objectvieDto) {
        return !objectvieDto.getTitle().trim().isEmpty();
    }

    public ObjectiveResponse setObjective(Objective objective, List<KeyResultResponse> keyResults) {
        return ObjectiveResponse.builder().id(objective.getId())
                .title(objective.getName())
                .userId(objective.getExecute().getUser().getId())
                .projectId(objective.getExecute().getProject() == null ? 0 :
                        objective.getExecute().getProject().getId())
                .alignmentObjectives(commonUtils.stringToArray(objective.getAlignmentObjectives()))
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
                .progress(keyResult.getProgress())
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

    private boolean checkDeleteAndUpdateObjective(Objective objective){
        Report report = reportRepository.findFirstByObjectiveId(objective.getId());
        Execute execute = objective.getExecute();
        if (report != null || execute.isDelete() || execute.isClose()
                || objective.getStatus().equalsIgnoreCase("completed")){
            return false;
        };
        return true;
    }

    private boolean checkUpdateForParentObjective(List<ObjectiveProjectItem> children,Objective parent){
        if(children.size() == 0){
            if(parent.getExecute().isDelete() || parent.getExecute().isClose()){
                return false;
            }
        }
       return children.stream().allMatch(ObjectiveProjectItem::isUpdate);
    }

    private List<MetaDataResponse> setListAlign(String alignStr){
        List<MetaDataResponse> responses = new ArrayList<>();
        if(alignStr == null || alignStr.isEmpty()){
            return responses;
        }
        List<Long> aligns = commonUtils.stringToArray(alignStr);

        List<Objective> objectives = objectiveRepository.findAllObjectiveByListId(aligns);

        objectives.forEach(objective -> {
            responses.add(
                    MetaDataResponse.builder()
                            .id(objective.getId())
                            .name(objective.getName())
                            .build()
            );
        });
        return responses;
    }
}
