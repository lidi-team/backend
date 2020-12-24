package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.DepartmentDto;
import capstone.backend.api.dto.ProjectPositionDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.Department;
import capstone.backend.api.entity.ProjectPosition;
import capstone.backend.api.repository.DepartmentRepository;
import capstone.backend.api.service.DepartmentService;
import capstone.backend.api.utils.CommonUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private DepartmentRepository departmentRepository;

    private CommonProperties commonProperties;
    private final CommonUtils commonUtils;


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

    @Override
    public ResponseEntity<?> getAllDepartment(int page, int limit, String text, String jwtToken) throws Exception {
        if (limit == 0){
            limit = 10;
        }

        Page<Department> departments;

        if (text == null) {
            departments = departmentRepository.findByIsDeleteFalse(PageRequest.of(page-1, limit, Sort.by("id")));
        } else{
            departments = departmentRepository.findByNameContainsAndIsDeleteFalse(text, PageRequest.of(page-1, limit, Sort.by("id")));
        }


        Map<String, Object> responses = new HashMap<>();
        List<Object> items = new ArrayList<>();


        departments.forEach(projectPosition -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", projectPosition.getId());
            item.put("name", projectPosition.getName());
            item.put("description", projectPosition.getDescription());
            items.add(item);
        });
        responses.put("items", items);
        responses.put("meta", commonUtils.paging(departments, page));
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses).build()
        );
    }

    @Override
    public ResponseEntity<?> createDepartment(DepartmentDto departmentDto, String jwtToken) throws Exception {
        Department departmentOld = departmentRepository.findByName(departmentDto.getName());
        Department data= new Department();
        if(departmentOld != null){
            if(departmentOld.isDelete()){
                departmentOld.setDelete(false);
                departmentOld.setName(departmentDto.getName());
                departmentOld.setDescription(departmentDto.getDescription());
                data= departmentRepository.save(departmentOld);
            } else{
                return ResponseEntity.ok().body(
                        ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                                .message("Tên phòng ban này đã tồn tại")
                                .build()
                );
            }
        }else {
            data = departmentRepository.save(Department.builder()
                    .name(departmentDto.getName())
                    .description(departmentDto.getDescription())
                    .build());
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(data)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> getDepartment(long id, String jwtToken) throws Exception {
        Department department = departmentRepository.findByIdAndIsDeleteFalse(id);

        if(department == null){
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }

        Map<String, Object> item = new HashMap<>();
        item.put("id", department.getId());
        item.put("name", department.getName());
        item.put("description", department.getDescription());

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(item).build()
        );
    }

    @Override
    public ResponseEntity<?> updateDepartment(long id, DepartmentDto departmentDto, String jwtToken) throws Exception {
        Department department = departmentRepository.findByIdAndIsDeleteFalse(id);

        if (department == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );
        }

        Department departmentOld = departmentRepository.findByName(departmentDto.getName());
        if(departmentOld != null){
            if(departmentOld.isDelete()){
                departmentOld.setDelete(false);
                departmentOld.setName(departmentDto.getName());
                departmentOld.setDescription(departmentDto.getDescription());
                departmentRepository.save(departmentOld);
            } else{
                return ResponseEntity.ok().body(
                        ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                                .message("Tên phòng ban này đã tồn tại")
                                .build()
                );
            }
        }else {
            departmentRepository.save(Department.builder()
                    .id(id)
                    .name(departmentDto.getName())
                    .description(departmentDto.getDescription())
                    .build());
        }
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );

    }

    @Override
    public ResponseEntity<?> deleteDepartment(long id, String jwtToken) throws Exception {
        Department department = departmentRepository.findByIdAndIsDeleteFalse(id);

        if(departmentRepository.checkExisted(id).size() > 0){
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                            .message("Đơn vị phòng ban này đang có nhân viên")
                            .build()
            );
        }

        if (department == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );
        }else {
            department.setDelete(true);
            departmentRepository.save(department);
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }


}
