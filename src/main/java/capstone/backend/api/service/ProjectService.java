package capstone.backend.api.service;

import capstone.backend.api.entity.ApiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService {
    ResponseEntity<?> getAllProjects() throws Exception;

    ResponseEntity<?> getListMetaDataProject() throws Exception;

    ResponseEntity<?> getAllAvailableProjectOfUser(String token,int type) throws Exception;

    ResponseEntity<?> getAllProjectPaging(int page, int limit, String sortWith, String type) throws Exception;

}
