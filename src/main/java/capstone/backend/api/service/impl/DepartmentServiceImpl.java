package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.controller.ObjectiveController;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.Department;
import capstone.backend.api.repository.DepartmentRepository;
import capstone.backend.api.service.DepartmentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private DepartmentRepository departmentRepository;

    private CommonProperties commonProperties;


    @Override
    public Department getDepartmentById(long id) throws Exception {
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<?> getListMetaDataDepartment() throws Exception {
        ArrayList<Department> departments = (ArrayList<Department>) departmentRepository.findAll();
        ArrayList<MetaDataResponse> responses = new ArrayList<>();
        departments.forEach(department -> {
            responses.add(
                    MetaDataResponse.builder()
                            .id(department.getId())
                            .name(department.getName())
                            .build()
            );
        });
        logger.info("get list meta data successful");
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses)
                        .build()
        );
    }


}
