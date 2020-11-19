package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.CheckinDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.KeyResult.KeyResultCheckin;
import capstone.backend.api.entity.ApiResponse.KeyResult.KeyResultResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.ApiResponse.Objective.ObjectiveCheckin;
import capstone.backend.api.entity.ApiResponse.Project.ProjectObjectiveResponse;
import capstone.backend.api.entity.ApiResponse.Report.Chart;
import capstone.backend.api.entity.ApiResponse.Report.ReportResponse;
import capstone.backend.api.entity.*;
import capstone.backend.api.repository.*;
import capstone.backend.api.service.ReportService;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final ExecuteRepository executeRepository;

    private final KeyResultRepository keyResultRepository;

    private final ObjectiveServiceImpl objectiveService;

    private final JwtUtils jwtUtils;

    @Override
    public ResponseEntity<?> getCheckinHistoryByObjectiveId(long id) {
        List<ReportResponse> responses = new ArrayList<>();
        List<Report> reports = reportRepository.findAllByObjectiveId(id);

        Objective objective = objectiveRepository.findByIdAndDelete(id);

        reports.forEach(report -> {
            responses.add(
                    ReportResponse.builder()
                            .id(report.getId())
                            .checkinAt(report.getCheckinDate())
                            .nextCheckinDate(report.getNextCheckinDate())
                            .status(setStatusForObjective(objective,report))
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

        Report report = Report.builder()
                .id(checkinDto.getId())
                .checkinDate(new Date())
                .nextCheckinDate(checkinDto.getNextCheckinDate())
                .objective(objective)
                .status(checkinDto.getStatus())
                .build();

        report = reportRepository.save(report);

        reportDetailService.addReportDetails(checkinDto.getCheckinDetails(), report);

        return null;
    }

    @Override
    public ResponseEntity<?> getListObjectiveByCycleId(String token, long cycleId) throws Exception {
        List<ProjectObjectiveResponse> responses = new ArrayList<>();

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        List<Execute> executes = executeRepository.findAllByUserIdAndOpenProject(user.getId());

        executes.forEach(execute -> {
            List<ObjectiveCheckin> objectivesResponse = new ArrayList<>();
            List<Objective> objectives = objectiveRepository.findAllByProjectIdAndCycleIdAndType(execute.getProject().getId(), cycleId, 2);

            setProjectResponse(responses, execute, objectivesResponse, objectives);

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
    public ResponseEntity<?> getCheckinDetailByObjectiveId(long id) {
        Map<String,Object> response = new HashMap<>();
        List<Chart> chart = new ArrayList<>();
        List<KeyResultCheckin> keyResultCheckins = new ArrayList<>();
        Map<String,Object> checkin = new HashMap<>();
        List<capstone.backend.api.entity.ApiResponse.Report.ReportDetail> details = new ArrayList<>();

        Objective objective = objectiveRepository.findByIdAndDelete(id);

        List<KeyResult> keyResults = keyResultRepository.findAllByObjectiveId(id);
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

        List<Report> reports = reportRepository.findAllByObjectiveId(id);
        reports.forEach(report -> {
            chart.add(
                    Chart.builder()
                            .checkinAt(report.getCheckinDate())
                            .progress(0)
                            .build()
            );
        });

        Report report = reportRepository.findFirstByObjectiveIdOrderByIdDesc(id);
        if(report != null){
            checkin.put("id",report.getId());
            checkin.put("checkinAt",report.getCheckinDate());
            checkin.put("nextCheckinDate", report.getNextCheckinDate());
            checkin.put("status",setStatusForObjective(objective,report));

            List<ReportDetail> reportDetails = detailRepository.findAllByReportId(report.getId());
            reportDetails.forEach(reportDetail -> {
                KeyResultCheckin keyResult =  keyResultCheckins.stream().filter(
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

        }

        response.put("id",objective.getId());
        response.put("title",objective.getName());
        response.put("progress",objective.getProgress());
        response.put("keyResults",keyResultCheckins);
        response.put("chart",chart);
        response.put("checkin",checkin);
        response.put("checkinDetail",details);

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response)
                        .build()
        );
    }

    private void setProjectResponse(List<ProjectObjectiveResponse> responses, Execute execute,
                                    List<ObjectiveCheckin> objectivesResponse, List<Objective> objectives) {
        objectives.forEach(objective -> {
            List<KeyResultResponse> keyResultResponses = new ArrayList<>();
            List<KeyResult> keyResults = keyResultRepository.findAllByObjectiveId(objective.getId());

            keyResults.forEach(keyResult -> {
                KeyResultResponse keyResultResponse = objectiveService.setKeyResult(keyResult);
                keyResultResponses.add(keyResultResponse);
            });

            Report report = reportRepository.findFirstByObjectiveIdOrderByIdDesc(objective.getId());
            String status = setStatusForObjective(objective,report);

            ObjectiveCheckin objectiveResponse = ObjectiveCheckin.builder()
                    .id(objective.getId())
                    .title(objective.getName())
                    .progress(objective.getProgress())
                    .change(objective.getChanging())
                    .checkinId(status.equalsIgnoreCase("Done") ? 0 : report.getId())
                    .status(status)
                    .keyResults(keyResultResponses)
                    .build();
            objectivesResponse.add(objectiveResponse);
        });

        responses.add(
                ProjectObjectiveResponse.builder()
                        .id(execute.getProject() == null ? 0 : execute.getProject().getId())
                        .name(execute.getProject() == null ? "Objective Công ty" : execute.getProject().getName())
                        .objectives(objectivesResponse)
                        .build()
        );
    }

    private String setStatusForObjective(Objective objective, Report report){
        if(objective.getStatus().equalsIgnoreCase("completed")){
            return "Completed";
        }

        if(report == null){
            return "Done";
        }

        if(report.getNextCheckinDate().before(new Date()) && report.getStatus().equalsIgnoreCase("draft")){
            return "Overdue";
        }

        return report.getStatus();
    }
}
