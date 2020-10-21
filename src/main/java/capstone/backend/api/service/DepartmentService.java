package capstone.backend.api.service;

import capstone.backend.api.entity.Department;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DepartmentService {
    Department getDepartmentById(long id) throws Exception;

    ResponseEntity<?> getListMetaDataDepartment() throws Exception;
}
