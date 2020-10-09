package capstone.backend.api.service.impl;

import capstone.backend.api.entity.Department;
import capstone.backend.api.repository.DepartmentRepository;
import capstone.backend.api.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;

    @Override
    public Department getDepartmentById(long id) throws Exception {
        return departmentRepository.findById(id).orElse(null);
    }
}
