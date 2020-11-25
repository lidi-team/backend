package capstone.backend.api.service;

import capstone.backend.api.dto.CheckinDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ReportService {

    ResponseEntity<?> getCheckinHistoryByObjectiveId(long id);

    ResponseEntity<?> addCheckin(CheckinDto checkinDto) throws Exception;

    ResponseEntity<?> getListObjectiveByCycleId(String token, long cycleId,long projectId,int page, int limit) throws Exception;

    ResponseEntity<?> getCheckinDetailByObjectiveId(long id) throws Exception;

    ResponseEntity<?> getDetailCheckinByCheckinId(long id) throws Exception;

    ResponseEntity<?> getListRequestCheckin(String token,int page,int limit,long cycleId) throws Exception;

}
