package capstone.backend.api.service;

import capstone.backend.api.dto.CreateCycleDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.Cycle;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface CycleService {

    Cycle getCycleById(long id) throws Exception;

    ResponseEntity<?> getAllCycles(int page, int limit, String text) throws Exception;

    ResponseEntity<?> createCycle(CreateCycleDto cycle) throws Exception;

    ResponseEntity<?> updateCycle(long id, CreateCycleDto cycleDto) throws Exception;

    ResponseEntity<?> deleteCycle(long id, String jwtToken) throws Exception;

    ResponseEntity<?> findById(long id) throws Exception;

    ResponseEntity<?> getListMetaDataCycle();

    ResponseEntity<?> getCurrentCycle(long id) throws Exception;

}
