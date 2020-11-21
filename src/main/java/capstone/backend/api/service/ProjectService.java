package capstone.backend.api.service;

import capstone.backend.api.dto.CreateProjectDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService {
    ResponseEntity<?> getAllProjects() throws Exception;

    ResponseEntity<?> getListMetaDataProject() throws Exception;

    ResponseEntity<?> getAllAvailableProjectOfUser(String token, int type) throws Exception;

    ResponseEntity<?> getAllProjectPaging(int page, int limit, String sortWith, String type,String text) throws Exception;

    ResponseEntity<?> getDetailProjectById(long id) throws Exception;

    ResponseEntity<?> createProject(CreateProjectDto projectDto) throws Exception;

}
