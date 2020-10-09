package capstone.backend.api.service;

import capstone.backend.api.entity.Department;
import org.springframework.stereotype.Service;

@Service
public interface DepartmentService {
    Department getDepartmentById(long id) throws Exception;
}
