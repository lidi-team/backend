package capstone.backend.api.service;

import capstone.backend.api.dto.DepartmentDto;
import capstone.backend.api.dto.ProjectPositionDto;
import capstone.backend.api.entity.Department;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface DepartmentService {
    Department getDepartmentById(long id) throws Exception;

    ResponseEntity<?> getListMetaDataDepartment() throws Exception;

    ResponseEntity<?> getAllDepartment(int page, int size, String text, String jwtToken) throws Exception;

    ResponseEntity<?> createDepartment(DepartmentDto departmentDto, String jwtToken) throws Exception;

    ResponseEntity<?> getDepartment(long id, String jwtToken) throws Exception;

    ResponseEntity<?> updateDepartment(long id, DepartmentDto departmentDto, String jwtToken) throws Exception;

    ResponseEntity<?> deleteDepartment(long id, String jwtToken) throws Exception;
}
