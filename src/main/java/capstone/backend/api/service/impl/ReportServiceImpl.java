package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.CheckinDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.KeyResult.KeyResultCheckin;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.ApiResponse.Objective.ObjectiveCheckin;
import capstone.backend.api.entity.ApiResponse.Objective.ObjectiveInferior;
import capstone.backend.api.entity.ApiResponse.Project.ProjectObjectiveResponse;
import capstone.backend.api.entity.ApiResponse.Report.*;
import capstone.backend.api.entity.*;
import capstone.backend.api.entity.ReportDetail;
import capstone.backend.api.repository.*;
import capstone.backend.api.service.ReportService;
import capstone.backend.api.utils.CommonUtils;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final CommonProperties commonProperties;

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepository reportRepository;

    private final ObjectiveRepository objectiveRepository;

    private final UserRepository userRepository;

    private final ReportDetailServiceImpl reportDetailService;

    private final ReportDetailRepository detailRepository;

    private final KeyResultRepository keyResultRepository;

    private final ExecuteRepository executeRepository;

    private final CycleRepository cycleRepository;

    private final JwtUtils jwtUtils;

    private final ProjectRepository projectRepository;

    private final CommonUtils commonUtils;

    @Override
    public ResponseEntity<?> getCheckinHistoryByObjectiveId(long id) {
        List<ReportResponse> responses = new ArrayList<>();
        List<Report> reports = reportRepository.findAllByObjectiveId(id);

        reports.forEach(report -> {
            responses.add(
                    ReportResponse.builder()
                            .id(report.getId())
                            .checkinAt(report.getCheckinDate())
                            .nextCheckinDate(report.getNextCheckinDate())
                            .status(report.getStatus())
                            .teamLeaderId(report.getAuthorizedUser().getId())
                            .objective(
                                    MetaDataResponse.builder()
                                            .id(report.getObjective().getId())
                                            .name(report.getObjective().getName())
                                            .build()
                            )
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
    public ResponseEntity<?> addCheckin(CheckinDto checkinDto) throws Exception {

        Objective objective = objectiveRepository.findByIdAndDelete(checkinDto.getObjectiveId());
        List<KeyResult> keyResults = keyResultRepository.findAllByObjectiveId(objective.getId());
        Map<Long, Double> valueOlds = saveOldValueKeyResult(keyResults);

        User authorizedUser = objective.getExecute().getUser();

        Report report;
        if (checkinDto.getId() != 0) {
            report = reportRepository.findById(checkinDto.getId()).get();
            report.setNextCheckinDate(commonUtils.stringToDate(checkinDto.getNextCheckinDate(), CommonUtils.PATTERN_ddMMyyyy));
            report.setAuthorizedUser(authorizedUser);
            report.setObjective(objective);
            report.setProgress(checkinDto.getProgress());
            report.setStatus(checkinDto.getStatus());
        } else {
            report = Report.builder()
                    .id(checkinDto.getId())
                    .nextCheckinDate(commonUtils.stringToDate(checkinDto.getNextCheckinDate(), CommonUtils.PATTERN_ddMMyyyy))
                    .objective(objective)
                    .progress(checkinDto.getProgress())
                    .authorizedUser(authorizedUser)
                    .status(checkinDto.getStatus())
                    .build();
        }

        if (checkinDto.getStatus().equalsIgnoreCase("Pending")) {
            report.setCheckinDate(new Date());
        }

        report = reportRepository.save(report);
        reportDetailService.addReportDetails(checkinDto.getCheckinDetails(), report, keyResults);

        if (checkinDto.getStatus().equalsIgnoreCase("Reviewed")) {
            double oldProgress = objective.getProgress();
            objective.setProgress(checkinDto.getProgress());
            objective.setChanging(objective.getProgress() - oldProgress);
            objectiveRepository.updateChangingAndProgressObjective(objective.getChanging(),
                    objective.getProgress(), objective.getId());

            Map<Long, Double> changing = setListChangingKeyResult(valueOlds, keyResults);
            //update progress of upper objectives and key results;
            Thread t = new Thread(() -> {
                calculateProgressAllObjective(objective, changing);
            });
            t.setDaemon(true);
            t.start();

        }

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getListObjectiveByCycleId(String token, long cycleId, long projectId, int page, int limit) throws Exception {
        if (limit == 0) {
            limit = 10;
        }
        if (page == 0) {
            page = 1;
        }
        Map<String,Object> response = new HashMap<>();
        List<ObjectiveCheckin> objectiveResponses = new ArrayList<>();

        Cycle cycle = cycleRepository.findById(cycleId).orElse(null);
        if (cycle == null)
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_PARAM_VALUE_INVALID())
                            .message(commonProperties.getMESSAGE_PARAM_VALUE_INVALID())
                            .build()
            );


        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();
        Page<Objective> objectives;
        if (projectId == 0) {
            objectives = objectiveRepository.findAllByTypeAndCycleIdAndUserId(
                    2,cycleId,user.getId(),PageRequest.of(page-1, limit));
        } else {
            objectives = objectiveRepository.findAllByTypeAndCycleIdAndUserIdAndProjectId(
                    2,cycleId,user.getId(),projectId,PageRequest.of(page-1, limit));
        }

        List<Objective> objectiveList = objectives.getContent();
        setObjectiveResponse(objectiveResponses, objectiveList);

        response.put("items",objectiveResponses);
        response.put("meta",commonUtils.paging(objectives,page));

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getCheckinDetailByObjectiveId(long id) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> checkin = new HashMap<>();
        List<capstone.backend.api.entity.ApiResponse.Report.ReportDetail> details = new ArrayList<>();

        Objective objective = objectiveRepository.findByIdAndDelete(id);

        List<KeyResultCheckin> keyResultCheckins = setListKeyResultCheckin(id);

        Chart chart = setListChartByObjectiveId(objective);

        Report report = reportRepository.findFirstByObjectiveIdOrderByIdDesc(objective.getId());
        if (report != null) {
            checkin = setCheckinListByObjectiveId(objective, report);
            details = setListDetailByReportId(report.getId(), keyResultCheckins);
        }

        response.put("id", objective.getId());
        response.put("title", objective.getName());
        response.put("progress", objective.getProgress());
        response.put("progressSuggest", setProgressSuggest(objective));
        response.put("keyResults", keyResultCheckins);
        response.put("chart", chart);
        response.put("checkin", checkin);
        response.put("checkinDetail", details);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getDetailCheckinByCheckinId(long id) throws Exception {
        Map<String, Object> response = new HashMap<>();
        Report report = reportRepository.findById(id).orElse(null);
        if (report == null)
            return ResponseEntity.status(401).body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );

        Objective objective = report.getObjective();
        Map<String, Object> objectiveMap = new HashMap<>();
        objectiveMap.put("id", objective.getId());
        objectiveMap.put("progress", objective.getProgress());
        objectiveMap.put("title", objective.getName());
        objectiveMap.put("userId", objective.getExecute().getUser().getId());

        List<KeyResultCheckin> keyResultCheckins = setListKeyResultCheckin(objective.getId());

        List<capstone.backend.api.entity.ApiResponse.Report.ReportDetail>
                reportDetails = setListDetailByReportId(id, keyResultCheckins);

        Chart chart = setListChartByObjectiveId(objective);

        response.put("id", report.getId());
        response.put("progress", report.getProgress());
        response.put("checkinAt", report.getCheckinDate());
        response.put("nextCheckinDate", report.getNextCheckinDate());
        response.put("status", report.getStatus());
        response.put("teamLeaderId", report.getAuthorizedUser().getId());
        response.put("objective", objectiveMap);
        response.put("checkinDetails", reportDetails);
        response.put("chart", chart);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getListRequestCheckin(String token, int page, int limit, long cycleId) throws Exception {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> items = new ArrayList<>();

        if (limit == 0) {
            limit = 10;
        }
        if (page == 0) {
            page = 1;
        }

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        Page<Report> reports = reportRepository.findByReviewerAndStatusAndCycle(
                user.getId(), "Pending", cycleId,
                PageRequest.of(page - 1, limit));
        for (Report report : reports) {

            ObjectiveCheckinRequest objective =
                    ObjectiveCheckinRequest.builder()
                            .id(report.getObjective().getId())
                            .title(report.getObjective().getName())
                            .user(
                                    UserRequestCheckin.builder()
                                            .id(report.getObjective().getExecute().getUser().getId())
                                            .fullName(report.getObjective().getExecute().getUser().getFullName())
                                            .build())
                            .build();

            MetaDataResponse project =
                    MetaDataResponse.builder()
                            .id(report.getObjective().getExecute().getProject().getId())
                            .name(report.getObjective().getExecute().getProject().getName())
                            .build();

            Map<String, Object> checkin = new HashMap<>();
            checkin.put("id", report.getId());
            checkin.put("createdAt", report.getCheckinDate());
            checkin.put("objective", objective);
            checkin.put("project", project);

            items.add(checkin);
        }
        response.put("items", items);
        response.put("meta", commonUtils.paging(reports, page));

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getListCheckinInferior(String token, int page, int limit, long cycleId, long projectId) throws Exception {
        Map<String,Object> response = new HashMap<>();
        List<Map<String,Object>> inferiors = new ArrayList<>();
        if (limit == 0) {
            limit = 10;
        }
        if (page == 0) {
            page = 1;
        }
        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();
        Page<Execute> pages;
        if(projectId == 0){
            pages = executeRepository.findExecuteByReviewerId(cycleId,
                    user.getId(),PageRequest.of(page -1,limit));
        } else {
            pages = executeRepository.findExecuteByReviewerIdAndProjectId(cycleId,
                            user.getId(),projectId,PageRequest.of(page -1,limit));
        }

        pages.getContent().forEach(execute -> {
            Map<String,Object> inferior = new HashMap<>();
            MetaDataResponse position = MetaDataResponse.builder()
                    .id(execute.getPosition().getId())
                    .name(execute.getPosition().getName())
                    .build();
            MetaDataResponse project = MetaDataResponse.builder()
                    .id(execute.getProject().getId())
                    .name(execute.getProject().getName())
                    .build();

            inferior.put("id",execute.getUser().getId());
            inferior.put("fullName",execute.getUser().getFullName());
            inferior.put("urlImage",execute.getUser().getAvatarImage());
            inferior.put("position",position);
            inferior.put("project",project);

            inferiors.add(inferior);
        });

        response.put("items",inferiors);
        response.put("meta",commonUtils.paging(pages,page));

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getListObjectiveInferior(long userId, long cycleId, long projectId) throws Exception {
        List<ObjectiveInferior> responses = new ArrayList<>();

        Page<Objective> objectives = objectiveRepository.findAllByTypeAndCycleIdAndUserIdAndProjectId(2,cycleId,userId,projectId,PageRequest.of(0,20));
        objectives.getContent().forEach(objective -> {
            ObjectiveInferior item = ObjectiveInferior.builder()
                    .id(objective.getId())
                    .progress(objective.getProgress())
                    .title(objective.getName())
                    .createAt(objective.getCreateAt())
                    .build();

            responses.add(item);

        });
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses)
                        .build()
        );
    }

    private void setObjectiveResponse(List<ObjectiveCheckin> objectivesResponse, List<Objective> objectives) {
        objectives.forEach(objective -> {
            List<Map<String, Object>> keyResultResponses = new ArrayList<>();
            List<KeyResult> keyResults = keyResultRepository.findAllByObjectiveId(objective.getId());

            keyResults.forEach(keyResult -> {
                Map<String, Object> keyResultResponse = new HashMap<>();
                keyResultResponse.put("id", keyResult.getId());
                keyResultResponse.put("startValue", keyResult.getFromValue());
                keyResultResponse.put("valueObtained", keyResult.getValueObtained());
                keyResultResponse.put("targetedValue", keyResult.getToValue());
                keyResultResponse.put("progress", keyResult.getProgress());
                keyResultResponse.put("content", keyResult.getContent());
                keyResultResponse.put("measureUnit", keyResult.getUnitOfKeyResult().getName());
                keyResultResponse.put("reference", keyResult.getReference());

                keyResultResponses.add(keyResultResponse);
            });

            Report report = reportRepository.findFirstByObjectiveIdOrderByIdDesc(objective.getId());
            String status = setStatusForObjective(objective, report);

            MetaDataResponse projectResponse = MetaDataResponse.builder()
                    .id(objective.getExecute().getProject().getId())
                    .name(objective.getExecute().getProject().getName())
                    .build();

            ObjectiveCheckin objectiveResponse = ObjectiveCheckin.builder()
                    .id(objective.getId())
                    .title(objective.getName())
                    .progress(objective.getProgress())
                    .change(objective.getChanging())
                    .checkinId(status.equalsIgnoreCase("Reviewed") ? 0 : report.getId())
                    .status(status)
                    .keyResults(keyResultResponses)
                    .project(projectResponse)
                    .build();
            objectivesResponse.add(objectiveResponse);
        });
    }

    private String setStatusForObjective(Objective objective, Report report) {
        if (objective.getStatus().equalsIgnoreCase("completed")) {
            return "Completed";
        }

        if (report == null) {
            return "Reviewed";
        }

        if (report.getNextCheckinDate().before(new Date()) && report.getStatus().equalsIgnoreCase("draft")) {
            return "Overdue";
        }

        return report.getStatus();
    }

    private Chart setListChartByObjectiveId(Objective objective) {
        Chart chart = new Chart();
        List<Double> progress = new ArrayList<>();
        List<Date> checkinAt = new ArrayList<>();
        List<Report> reports = reportRepository.findAllByObjectiveIdAndStatusContains(objective.getId(),"Reviewed");
        checkinAt.add(objective.getCreateAt());
        progress.add(0.0);

        reports.forEach(report -> {
            progress.add(report.getProgress());
            checkinAt.add(report.getCheckinDate());
        });
        chart.setProgress(progress);
        chart.setCheckinAt(checkinAt);
        return chart;
    }

    private Map<String, Object> setCheckinListByObjectiveId(Objective objective, Report report) {
        Map<String, Object> checkin = new HashMap<>();
        checkin.put("id", report.getId());
        checkin.put("checkinAt", report.getCheckinDate());
        checkin.put("nextCheckinDate", report.getNextCheckinDate());
        checkin.put("status", setStatusForObjective(objective, report));

        return checkin;
    }

    private List<capstone.backend.api.entity.ApiResponse.Report.ReportDetail> setListDetailByReportId(long reportId,
                                                                                                      List<KeyResultCheckin> keyResultCheckins) {
        List<capstone.backend.api.entity.ApiResponse.Report.ReportDetail> details = new ArrayList<>();
        List<ReportDetail> reportDetails = detailRepository.findAllByReportId(reportId);
        reportDetails.forEach(reportDetail -> {
            KeyResultCheckin keyResult = keyResultCheckins.stream().filter(
                    item -> item.getId() == reportDetail.getKeyResult().getId())
                    .collect(Collectors.toList()).get(0);
            details.add(
                    capstone.backend.api.entity.ApiResponse.Report.ReportDetail.builder()
                            .id(reportDetail.getId())
                            .confidentLevel(reportDetail.getConfidentLevel())
                            .progress(reportDetail.getProgress())
                            .problems(reportDetail.getProblems())
                            .plans(reportDetail.getPlans())
                            .valueObtained(reportDetail.getValueObtained())
                            .keyResult(keyResult)
                            .build()
            );
        });
        return details;
    }

    private List<KeyResultCheckin> setListKeyResultCheckin(long objectiveId) {
        List<KeyResultCheckin> keyResultCheckins = new ArrayList<>();
        List<KeyResult> keyResults = keyResultRepository.findAllByObjectiveId(objectiveId);
        keyResults.forEach(keyResult -> {
            keyResultCheckins.add(
                    KeyResultCheckin.builder()
                            .id(keyResult.getId())
                            .content(keyResult.getContent())
                            .targetedValue(keyResult.getToValue())
                            .valueObtained(keyResult.getValueObtained())
                            .build()
            );
        });

        return keyResultCheckins;
    }

    private double setProgressSuggest(Objective objective) {
        double progress;
        double weight = 0;
        double totalProgress = 0;

        if (objective.getType() == 2) {
            List<KeyResult> keyResults = keyResultRepository.findAllByObjectiveId(objective.getId());
            weight = keyResults.size() == 0 ? 1 : keyResults.size();
            totalProgress = keyResults.stream().mapToDouble(KeyResult::calculateProgress).sum();

        } else {
            List<Project> projects = new ArrayList<>();
            if (objective.getType() == 1) {
                List<Objective> objectives =
                        objectiveRepository.findAllByParentIdAndType(objective.getId(), commonProperties.getOBJ_PERSONAL());
                //calculate by personal objective of project
                weight = objectives.stream().mapToDouble(Objective::getWeight).sum();
                totalProgress = objectives.stream().mapToDouble(Objective::calculateProgress).sum();

                //get list children projects of current project
                long currentProjectId = objective.getExecute().getProject().getId();
                projects = projectRepository.findAllByParentId(currentProjectId);

            } else {
                //get list projects in this cycle
                projects = projectRepository.findAllByFromDateAndEndDate(objective.getCycle().getFromDate());

            }

            for (Project project : projects) {
                List<Objective> objectives = objectiveRepository.findAllByProjectIdAndCycleIdAndType(
                        project.getId(), objective.getCycle().getId(),
                        commonProperties.getOBJ_PROJECT());

                weight += objectives.stream().mapToDouble(Objective::getWeight).sum() * project.getWeight();
                totalProgress += objectives.stream().mapToDouble(Objective::calculateProgress).sum() * project.getWeight();
            }
        }
        weight = weight == 0 ? 1 : weight;
        progress = totalProgress / weight;

        return progress;
    }

    private void calculateProgressAllObjective(Objective objective, Map<Long, Double> keyResultChanging) {
        Objective parentObjective = objectiveRepository.findById(objective.getParentId()).get();

        double change = objective.getChanging() * calculatePercentObjective(objective, parentObjective);
        double progress = parentObjective.getProgress() + change;

        parentObjective.setChanging(change);
        parentObjective.setProgress(progress);
        objectiveRepository.updateChangingAndProgressObjective(change, progress, parentObjective.getId());

        List<KeyResult> parentKeyResults = keyResultRepository.findAllByObjectiveId(parentObjective.getId());
        Map<Long, Double> oldParentValue = saveOldValueKeyResult(parentKeyResults);

        for (KeyResult parentKeyResult : parentKeyResults) {
            if (keyResultChanging.containsKey(parentKeyResult.getId())) {
                double changeKey = keyResultChanging.get(parentKeyResult.getId());
                double progressKey = parentKeyResult.getProgress() + changeKey * parentKeyResult.getProgress();
                double valueObtain = calculateValueObtainKeyResult(parentKeyResult);
                parentKeyResult.setProgress(progressKey);
                parentKeyResult.setValueObtained(valueObtain);
                keyResultRepository.updateKeyResultProgress(progressKey, valueObtain, parentKeyResult.getId());
            }
        }

        if (parentObjective.getParentId() != 0) {
            Map<Long, Double> changingList = setListChangingKeyResult(oldParentValue, parentKeyResults);
            calculateProgressAllObjective(parentObjective, changingList);
        }

    }

    private double calculatePercentObjective(Objective child, Objective parent) {
        double totalWeight = 0;
        List<Objective> children = objectiveRepository.findAllByParentId(parent.getId());
        for (Objective objective : children) {
            if (objective.getType() == 1) {
                totalWeight += objective.getWeight() * objective.getExecute().getProject().getWeight();
            } else {
                totalWeight += objective.getWeight();
            }
        }
        if (child.getType() == 1) {
            return child.getWeight() * child.getExecute().getProject().getWeight() / totalWeight;
        } else {
            return child.getWeight() / totalWeight;
        }
    }

    private Map<Long, Double> setListChangingKeyResult(Map<Long, Double> olds, List<KeyResult> news) {
        Map<Long, Double> map = new HashMap<>();
        for (KeyResult keyResult : news) {
            double oldProgress = olds.get(keyResult.getId());
            map.put(keyResult.getParentId(), keyResult.getProgress() - oldProgress);
        }
        return map;
    }

    private Map<Long, Double> saveOldValueKeyResult(List<KeyResult> olds) {
        Map<Long, Double> oldValue = new HashMap<>();
        olds.forEach(old -> {
            oldValue.put(old.getId(), old.getProgress());
        });
        return oldValue;
    }

    private double calculateValueObtainKeyResult(KeyResult keyResult) {
        return keyResult.getProgress() * (keyResult.getToValue() - keyResult.getFromValue()) + keyResult.getFromValue();
    }
}
