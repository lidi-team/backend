package capstone.backend.api.service;

import capstone.backend.api.dto.CreateCfrDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CfrService {

    ResponseEntity<?> getListWaiting(int page, int limit,String token) throws Exception;

    ResponseEntity<?> getHistoryCfrs(int page, int limit,long cycleId,int type,String token) throws Exception;

    ResponseEntity<?> getUserStar(long cycleId) throws Exception;

    ResponseEntity<?> getDetailCfr(long id) throws Exception;

    ResponseEntity<?> createCfr(CreateCfrDto dto) throws Exception;
}
