package capstone.backend.api.service;

import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.Cycle;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CycleService {

    Cycle getCycleById(long id) throws Exception;

    ResponseEntity<ApiResponse> getListMetaDataCycle();

    ResponseEntity<ApiResponse> getCurrentCycle(long id);

}
