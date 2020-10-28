package capstone.backend.api.service;

import capstone.backend.api.dto.CheckinDetailDto;
import capstone.backend.api.entity.Report;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReportDetailService {

    void addReportDetails(List<CheckinDetailDto> list, Report report) throws Exception;
}
