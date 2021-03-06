package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.MeasureDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.Department;
import capstone.backend.api.entity.UnitOfKeyResult;
import capstone.backend.api.repository.UnitOfKeyResultRepository;
import capstone.backend.api.service.UnitOfKeyResultService;
import capstone.backend.api.utils.CommonUtils;
import lombok.AllArgsConstructor;
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
public class UnitOfKeyResultServiceImpl implements UnitOfKeyResultService {

    UnitOfKeyResultRepository unitRepository;

    private CommonProperties commonProperties;


    private final CommonUtils commonUtils;

    @Override
    public UnitOfKeyResult getUnitById(long id) {
        return unitRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<?> getAllMeasure(int page, int size, String text, String jwtToken) throws Exception {
        if (size <= 0) {
            size = 10;
        }

        if (page < 1) {
            page = 1;
        }

        Page<UnitOfKeyResult> measureList;
        if (text == null) {
            measureList = unitRepository.findByIsDeleteFalse(PageRequest.of(page - 1, size, Sort.by("id").ascending()));
        } else {
            measureList = unitRepository.findByNameContainsAndIsDeleteFalse(text, PageRequest.of(page - 1, size, Sort.by("id").ascending()));
        }
        Map<String, Object> responses = new HashMap<>();
        List<Object> items = new ArrayList<>();


        measureList.forEach(measure -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", measure.getId());
            item.put("present", measure.getPreset());
            item.put("name", measure.getName());
            item.put("type", measure.getName());
            item.put("index", measure.getMeasureIndex());
            items.add(item);
        });
        responses.put("items", items);
        responses.put("meta", commonUtils.paging(measureList, page));

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses).build()
        );
    }

    @Override
    public ResponseEntity<?> createMeasure(MeasureDto unit, String jwtToken) throws Exception {

        List<UnitOfKeyResult> units = unitRepository.findAllDeleteFalse();

        for (UnitOfKeyResult unitOfKeyResult : units) {
            if(unitOfKeyResult.getName().equalsIgnoreCase(unit.getType())){
                return ResponseEntity.ok().body(
                        ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                                .message("Tên đơn vị đo lường đã tồn tại").build()
                );
            }
        }

        UnitOfKeyResult data = unitRepository.save(
                UnitOfKeyResult.builder()
                        .name(unit.getType())
                        .preset(unit.getPresent())
                        .measureIndex(unit.getIndex())
                        .build());


        Map<String, Object> item = new HashMap<>();
        item.put("id", data.getId());
        item.put("present", data.getPreset());
        item.put("type", data.getName());
        item.put("index", data.getMeasureIndex());

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(item).build()
        );
    }

    @Override
    public ResponseEntity<?> getMeasureById(long id, String jwtToken) {
        UnitOfKeyResult measure = unitRepository.findByIdAndIsDeleteFalse(id);

        if (measure == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> item = new HashMap<>();
        item.put("id", measure.getId());
        item.put("present", measure.getPreset());
        item.put("type", measure.getName());
        item.put("index", measure.getMeasureIndex());

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(item).build()
        );
    }

    @Override
    public ResponseEntity<?> updateMeasure(long id, MeasureDto unit, String jwtToken) throws Exception {
        UnitOfKeyResult unitOfKeyResult = unitRepository.findByIdAndIsDeleteFalse(id);

        if (unitOfKeyResult == null) {
            return ResponseEntity.notFound().build();
        }

        List<UnitOfKeyResult> units = unitRepository.findAllDeleteFalse();
        for (UnitOfKeyResult ofKeyResult : units) {
            if(ofKeyResult.getId() != id && ofKeyResult.getName().equalsIgnoreCase(unit.getType())){
                return ResponseEntity.ok().body(
                        ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                                .message("Tên đơn vị đo lường đã tồn tại").build()
                );
            }
        }

        unitOfKeyResult.setPreset(unit.getPresent());
        unitOfKeyResult.setName(unit.getType());
        unitOfKeyResult.setMeasureIndex(unit.getIndex());
        unitRepository.save(unitOfKeyResult);

        Map<String, Object> item = new HashMap<>();
        item.put("id", unitOfKeyResult.getId());
        item.put("present", unitOfKeyResult.getPreset());
        item.put("type", unitOfKeyResult.getName());
        item.put("index", unitOfKeyResult.getMeasureIndex());

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(item).build()
        );
    }

    @Override
    public ResponseEntity<?> deleteMeasure(long id, String jwtToken) throws Exception {
        UnitOfKeyResult unitOfKeyResult = unitRepository.findByIdAndIsDeleteFalse(id);

        if (unitRepository.checkExisted(unitOfKeyResult.getId()).size() > 0) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                            .message("Đơn vị này đang được sử dụng").build()
            );
        }

        unitOfKeyResult.setDelete(true);
        unitRepository.save(unitOfKeyResult);

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_UPDATE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> searchMeasure(String name, int page, int size, String sort, String jwtToken) {
        if (sort == null) {
            sort = "id";
        }

        Page<UnitOfKeyResult> measureList = unitRepository.
                findByNameContainsAndIsDeleteFalse(name, PageRequest.of(page, size, Sort.by(sort)));

        Map<String, Object> responses = new HashMap<>();
        List<Object> items = new ArrayList<>();


        measureList.forEach(measure -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", measure.getId());
            item.put("present", measure.getPreset());
            item.put("type", measure.getName());
            item.put("index", measure.getMeasureIndex());
            items.add(item);
        });
        responses.put("items", items);
        responses.put("meta", commonUtils.paging(measureList, page));

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses).build()
        );
    }

    @Override
    public ResponseEntity<?> getListMetaDataMeasureUnit() throws Exception {
        List<UnitOfKeyResult> units = unitRepository.findAllDeleteFalse();
        ArrayList<MetaDataResponse> responses = new ArrayList<>();
        units.forEach(unit -> {
            responses.add(
                    MetaDataResponse.builder()
                            .id(unit.getId())
                            .name(unit.getName())
                            .build()
            );
        });
        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(responses).build()
        );
    }


}
