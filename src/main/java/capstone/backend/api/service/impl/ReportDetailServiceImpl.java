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
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportDetailServiceImpl implements ReportDetailService {

    ReportDetailRepository detailRepository;

    KeyResultRepository keyResultRepository;

    @Override
    public void addReportDetails(List<CheckinDetailDto> list, Report report) throws Exception {
        List<ReportDetail> details = new ArrayList<>();
        List<KeyResult> keyResults = new ArrayList<>();

        list.forEach(item ->{
            KeyResult keyResult = keyResultRepository.findById(item.getKeyResultId()).orElse(null);
            details.add(
                    ReportDetail.builder()
                            .id(item.getId())
                            .confidentLevel(item.getConfidentLevel())
                            .progress(item.getProgress())
                            .plans(item.getPlan())
                            .problems(item.getProblem())
                            .targetValue(item.getTargetValue())
                            .valueObtained(item.getValueObtained())
                            .report(report)
                            .keyResult(keyResult)
                            .build()
            );
            if(keyResult != null){
                keyResult.setValueObtained(item.getValueObtained());
                keyResult.setProgress(Math.abs((item.getValueObtained() - keyResult.getFromValue())
                        /(keyResult.getToValue() - keyResult.getFromValue())));

                keyResults.add(keyResult);
            }
        });

        detailRepository.saveAll(details);
        keyResultRepository.saveAll(keyResults);
    }
}
