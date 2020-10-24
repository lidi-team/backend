package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.Cycle;
import capstone.backend.api.repository.CycleRepository;
import capstone.backend.api.service.CycleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CycleServiceImpl implements CycleService {

    private CycleRepository cycleRepository;

    private CommonProperties commonProperties;

    @Override
    public Cycle getCycleById(long id) throws Exception {
        return cycleRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<?> getAllCycles(int page, int size, String sort, String jwtToken) throws Exception {
        List<Cycle> cycleList = cycleRepository.findAll(PageRequest.of(page, size, Sort.by(sort).ascending())).toList();

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(cycleList).build()
        );
    }

    @Override
    public ResponseEntity<?> createCycle(Cycle cycle, String jwtToken) throws Exception {
        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(cycleRepository.save(cycle)).build()
        );
    }

    @Override
    public ResponseEntity<?> updateCycle(Cycle cycle, String jwtToken) throws Exception {
        Cycle currentCycle = cycleRepository.findById(cycle.getId()).orElse(null);

        if (currentCycle == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }else{
            currentCycle = cycle;
            cycleRepository.save(currentCycle);
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(cycleRepository.findById(cycle.getId())).build()
        );
    }

    @Override
    public ResponseEntity<?> deleteCycle(long id, String jwtToken) throws Exception {
        Cycle currentCycle = cycleRepository.findById(id).orElse(null);

        if (currentCycle == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }else{
            currentCycle.setDelete(true);
            cycleRepository.save(currentCycle);
        }

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(cycleRepository.findById(id)).build()
        );
    }

    @Override
    public ResponseEntity<?> searchCycle(Date date, int page, int size, String sort, String jwtToken) throws Exception {
        List<Cycle> cycleList = cycleRepository.
                findAllByFromDateLessThanEqualAndEndDateGreaterThanEqual(date, date, PageRequest.of(page, size, Sort.by(sort))).toList();

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(cycleList).build()
        );
    }
}
