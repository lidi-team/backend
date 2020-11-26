package capstone.backend.api.service;

import capstone.backend.api.dto.ProjectPositionDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ProjectPositionService {

    ResponseEntity<ApiResponse> getListMetaDataPosition() throws Exception;

    ResponseEntity<ApiResponse> getAllPosition(int page, int size, String text, String jwtToken) throws Exception;

    ResponseEntity<ApiResponse> createPosition(ProjectPositionDto positionDto, String jwtToken) throws Exception;

    ResponseEntity<ApiResponse> updatePosition(long id, ProjectPositionDto positionDto, String jwtToken) throws Exception;

    ResponseEntity<ApiResponse> deletePosition(long id, String jwtToken) throws Exception;

    ResponseEntity<ApiResponse> getPosition(long id, String jwtToken) throws Exception;
}
