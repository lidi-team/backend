package capstone.backend.api.service.impl;

import capstone.backend.api.dto.CheckinDetailDto;
import capstone.backend.api.entity.KeyResult;
import capstone.backend.api.entity.Report;
import capstone.backend.api.entity.ReportDetail;
import capstone.backend.api.repository.KeyResultRepository;
import capstone.backend.api.repository.ReportDetailRepository;
import capstone.backend.api.service.ReportDetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportDetailServiceImpl implements ReportDetailService {

    ReportDetailRepository detailRepository;

    KeyResultRepository keyResultRepository;

    @Override
    public void addReportDetails(List<CheckinDetailDto> list, Report report, List<KeyResult> keyResults) throws Exception {
        List<ReportDetail> details = new ArrayList<>();

        list.forEach(item -> {
            KeyResult keyResult = keyResults.stream()
                    .filter(keyResult1 -> keyResult1.getId() == item.getKeyResultId()).findFirst().orElse(null);

            details.add(
                    ReportDetail.builder()
                            .id(item.getId())
                            .confidentLevel(item.getConfidentLevel())
                            .progress(item.getProgress())
                            .plans(item.getPlans())
                            .problems(item.getProblems())
                            .targetValue(item.getTargetValue())
                            .valueObtained(item.getValueObtained())
                            .report(report)
                            .keyResult(keyResult)
                            .build()
            );

            if (keyResult != null && report.getStatus().equalsIgnoreCase("Reviewed")) {
                double valueObtain = item.getValueObtained();
                double progress = keyResult.calculateProgress() * item.getConfidentLevel();
                keyResultRepository.updateKeyResultProgress(progress,valueObtain,keyResult.getId());

                int i = keyResults.indexOf(keyResult);
                keyResults.get(i).setValueObtained(valueObtain);
                keyResults.get(i).setProgress(progress);

            }
        });
        detailRepository.saveAll(details);
    }
}
