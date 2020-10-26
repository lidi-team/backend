package capstone.backend.api.service;

import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.Cycle;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface CycleService {

    Cycle getCycleById(long id) throws Exception;

    ResponseEntity<?> getAllCycles(int page, int size, String sort, String jwtToken) throws Exception;

    ResponseEntity<?> createCycle(Cycle cycle, String jwtToken) throws Exception;

    ResponseEntity<?> updateCycle(Cycle cycle, String jwtToken) throws Exception;

    ResponseEntity<?> deleteCycle(long id, String jwtToken) throws Exception;

    ResponseEntity<?> searchCycle(Date date, int page, int size, String sort, String jwtToken)throws Exception;
    ResponseEntity<ApiResponse> getListMetaDataCycle();

    ResponseEntity<ApiResponse> getCurrentCycle(long id);

}
