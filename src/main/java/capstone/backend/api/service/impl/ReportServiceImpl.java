package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.CheckinDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.KeyResultResponse;
import capstone.backend.api.entity.ApiResponse.Objective.ObjectiveResponse;
import capstone.backend.api.entity.ApiResponse.ProjectObjectiveResponse;
import capstone.backend.api.entity.ApiResponse.ReportResponse;
import capstone.backend.api.entity.*;
import capstone.backend.api.repository.*;
import capstone.backend.api.service.ReportService;
import capstone.backend.api.utils.security.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private CommonProperties commonProperties;

    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private ReportRepository reportRepository;

    private ObjectiveRepository objectiveRepository;

    private UserRepository userRepository;

    private ReportDetailServiceImpl reportDetailService;

    private ExecuteRepository executeRepository;

    private KeyResultRepository keyResultRepository;

    private ObjectiveServiceImpl objectiveService;

    private JwtUtils jwtUtils;

    @Override
    public ResponseEntity<?> getCheckinHistoryByObjectiveId(long id) {
        List<ReportResponse> responses = new ArrayList<>();
        List<Report> reports = reportRepository.findAllByObjectiveId(id);

        reports.forEach(report -> {
            responses.add(
                    ReportResponse.builder()
                            .id(report.getId())
                            .checkinDate(report.getCheckinDate())
                            .nextCheckinDate(report.getNextCheckinDate())
                            .title(report.getObjective().getName())
                            .status(report.getStatus())
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
    public ResponseEntity<?> getListObjectiveByCycleId(String token, long cycleId, int type) throws Exception {
        List<ProjectObjectiveResponse> responses = new ArrayList<>();

        String email = jwtUtils.getUserNameFromJwtToken(token.substring(5));
        User user = userRepository.findByEmail(email).get();

        List<Execute> executes = executeRepository.findAllByUserIdAndOpenProject(user.getId());

        executes.forEach(execute -> {
            List<ObjectiveResponse> objectivesResponse = new ArrayList<>();
            List<Objective> objectives = objectiveRepository.findAllByProjectIdAndCycleIdAndType(execute.getProject().getId(), cycleId, type);

            if(type == 1 && execute.getPosition().getName().equalsIgnoreCase("project manager")){
                setProjectResponse(responses, execute, objectivesResponse, objectives);
            } else if(type == 2){
                setProjectResponse(responses, execute, objectivesResponse, objectives);
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

    private void setProjectResponse(List<ProjectObjectiveResponse> responses, Execute execute,
                                    List<ObjectiveResponse> objectivesResponse, List<Objective> objectives) {
        objectives.forEach(objective -> {
            List<KeyResultResponse> keyResultResponses = new ArrayList<>();
            List<KeyResult> keyResults = keyResultRepository.findAllByObjectiveId(objective.getId());

            keyResults.forEach(keyResult -> {
                KeyResultResponse keyResultResponse = objectiveService.setKeyResult(keyResult);
                keyResultResponses.add(keyResultResponse);
            });

            ObjectiveResponse objectiveResponse = objectiveService.setObjective(objective, keyResultResponses);
            objectivesResponse.add(objectiveResponse);
        });

        responses.add(
                ProjectObjectiveResponse.builder()
                        .id(execute.getProject() == null ? 0 : execute.getProject().getId())
                        .name(execute.getProject() == null ? "Objective Công ty" : execute.getProject().getName())
                        .position(execute.getPosition().getName())
                        .objectives(objectivesResponse)
                        .build()
        );
    }
}
