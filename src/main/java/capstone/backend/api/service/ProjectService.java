package capstone.backend.api.service;

import capstone.backend.api.dto.AddStaffToProjectDto;
import capstone.backend.api.dto.CreateProjectDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {
    ResponseEntity<?> getAllProjects() throws Exception;

    ResponseEntity<?> getListMetaDataProject() throws Exception;

    ResponseEntity<?> getAllAvailableProjectOfUser(String token) throws Exception;

    ResponseEntity<?> getAllProjectPaging(int page, int limit, String sortWith, String type,String text) throws Exception;

    ResponseEntity<?> getDetailProjectById(long id) throws Exception;

    ResponseEntity<?> createProject(CreateProjectDto projectDto) throws Exception;

    ResponseEntity<?> getListParentProject() throws Exception;

    ResponseEntity<?> getListStaffForPm(String text) throws Exception;

    ResponseEntity<?> updateListStaff(List<AddStaffToProjectDto> dtos, long projectId) throws Exception;

    ResponseEntity<?> getListStaffByProjectId(long projectId) throws Exception;

    ResponseEntity<?> addStaffToProject(List<Long> ids, long projectId) throws Exception;

    ResponseEntity<?> removeStaff(long projectId, long userId) throws Exception;

    ResponseEntity<?> getListCandidate(long projectId) throws Exception;

    ResponseEntity<?> getAllProjectManagePaging(int page, int limit, String sortWith, String type,String text,String token) throws Exception;
}
