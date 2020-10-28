package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.UnitOfKeyResult;
import capstone.backend.api.repository.UnitOfKeyResultRepository;
import capstone.backend.api.service.UnitOfKeyResultService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UnitOfKeyResultServiceImpl implements UnitOfKeyResultService {

    UnitOfKeyResultRepository unitRepository;

    private CommonProperties commonProperties;

    @Override
    public UnitOfKeyResult getUnitById(long id) {
        return unitRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<?> getAllMeasure(int page, int size, String sort, String jwtToken) throws Exception {
        List<UnitOfKeyResult> measureList = unitRepository.findAll(PageRequest.of(page, size, Sort.by(sort).ascending())).toList();

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(measureList).build()
        );
    }

    @Override
    public ResponseEntity<?> createMeasure(UnitOfKeyResult unit, String jwtToken) throws Exception {
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(unitRepository.save(unit)).build()
        );
    }

    @Override
    public ResponseEntity<?> updateMeasure(UnitOfKeyResult unit, String jwtToken) throws Exception {
        UnitOfKeyResult unitOfKeyResult = unitRepository.findById(unit.getId()).orElse(null);

        if (unitOfKeyResult == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        } else {
            unitOfKeyResult = unit;
            unitRepository.save(unitOfKeyResult);
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(unitRepository.findById(unit.getId())).build()
        );
    }

    @Override
    public ResponseEntity<?> deleteMeasure(long id, String jwtToken) throws Exception {
        UnitOfKeyResult unitOfKeyResult = unitRepository.findById(id).orElse(null);

        if (unitOfKeyResult == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        } else {
            unitOfKeyResult.setDelete(true);
            unitRepository.save(unitOfKeyResult);
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(unitRepository.findById(id)).build()
        );
    }

    @Override
    public ResponseEntity<?> searchMeasure(String name, int page, int size, String sort, String jwtToken) {
        List<UnitOfKeyResult> unitOfKeyResults = unitRepository.
                findByNameContains(name, PageRequest.of(page, size, Sort.by(sort))).toList();

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(unitOfKeyResults).build()
        );
    }

}
