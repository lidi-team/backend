package capstone.backend.api.service;

import capstone.backend.api.entity.ApiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ProjectPositionService {

    ResponseEntity<ApiResponse> getListMetaDataPosition() throws Exception;
}
