package capstone.backend.api.service.impl;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.CreateCycleDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.entity.ApiResponse.CycleResponse;
import capstone.backend.api.entity.ApiResponse.MetaDataResponse;
import capstone.backend.api.entity.Cycle;
import capstone.backend.api.repository.CycleRepository;
import capstone.backend.api.service.CycleService;
import capstone.backend.api.utils.CommonUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CycleServiceImpl implements CycleService {

    private final CycleRepository cycleRepository;

    private final CommonProperties commonProperties;

    private final CommonUtils utils;

    @Override
    public Cycle getCycleById(long id) throws Exception {
        Cycle cycle;
        if(id == 0){
            Date today = new Date();
            cycle = cycleRepository.findFirstByFromDateBeforeAndEndDateAfter(today, today);
        } else {
            cycle = cycleRepository.findById(id).orElse(null);
        }
        return cycle;
    }

    @Override
    public ResponseEntity<ApiResponse> getListMetaDataCycle() {
        List<Cycle> cycles = cycleRepository.findAll();
        ArrayList<MetaDataResponse> responses = new ArrayList<>();

        cycles.forEach(cycle -> {
            responses.add(
                    MetaDataResponse.builder()
                            .id(cycle.getId())
                            .name(cycle.getName())
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

    @Override
    public ResponseEntity<ApiResponse> getCurrentCycle(long id) throws Exception{
        Cycle cycle = getCycleById(id);
        if (cycle == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        }
        MetaDataResponse response = MetaDataResponse.builder()
                .id(cycle.getId())
                .name(cycle.getName()).build();

        return ResponseEntity.ok().body(
                ApiResponse.builder()
                        .code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    @Override
    public ResponseEntity<?> getAllCycles(int page, int size, String text) throws Exception {
        Map<String,Object> response = new HashMap<>();
        Page<Cycle> cycles;
        List<CycleResponse> items = new ArrayList<>();
        if(size == 0){
            size = 10;
        }
        if(text == null || text.isEmpty()){
            cycles = cycleRepository.findByIsDeleteFalse(PageRequest.of(page-1, size));
        }else {
            cycles = cycleRepository.findAllByNameContainsAndIsDeleteFalse(text,PageRequest.of(page-1, size));
        }

        List<Cycle> list = cycles.getContent();
        list.forEach(item ->{
            items.add(
                    CycleResponse.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .startDate(item.getFromDate())
                            .endDate(item.getEndDate())
                            .build()
            );
        });
        response.put("items",items);
        response.put("meta",utils.paging(cycles,page));

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }

    @Override
    public ResponseEntity<?> createCycle(CreateCycleDto cycleDto) throws Exception {

        String fromDateStr = cycleDto.getStartDate();
        String endDateStr = cycleDto.getEndDate();
        Date fromDate = utils.stringToDate(fromDateStr,CommonUtils.PATTERN_ddMMyyyy);
        Date endDate = utils.stringToDate(endDateStr,CommonUtils.PATTERN_ddMMyyyy);

        cycleRepository.save(
                Cycle.builder()
                .name(cycleDto.getName())
                .fromDate(fromDate)
                .endDate(endDate)
                .isDelete(false)
                .build());

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> updateCycle(long id, CreateCycleDto cycleDto) throws Exception {

        Cycle currentCycle = cycleRepository.findById(id).orElse(null);

        if (currentCycle == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND())
                            .build()
            );
        }

        String fromDateStr = cycleDto.getStartDate();
        String endDateStr = cycleDto.getEndDate();
        Date fromDate = utils.stringToDate(fromDateStr,CommonUtils.PATTERN_ddMMyyyy);
        Date endDate = utils.stringToDate(endDateStr,CommonUtils.PATTERN_ddMMyyyy);

        currentCycle = Cycle.builder()
                .id(currentCycle.getId())
                .name(cycleDto.getName())
                .fromDate(fromDate)
                .endDate(endDate)
                .build();
        cycleRepository.save(currentCycle);

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> deleteCycle(long id, String jwtToken) throws Exception {
        Cycle currentCycle = cycleRepository.findById(id).orElse(null);

        if(cycleRepository.checkExisted(id).size() > 0){
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder().code(commonProperties.getCODE_UPDATE_FAILED())
                            .message("Chu kì này đang được sử dụng").build()
            );
        }
        if (currentCycle == null) {
            return ResponseEntity.ok().body(
                    ApiResponse.builder().code(commonProperties.getCODE_NOT_FOUND())
                            .message(commonProperties.getMESSAGE_NOT_FOUND()).build()
            );
        } else {
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
    public ResponseEntity<?> findById(long id) throws Exception {
        Cycle cycle = cycleRepository.findByIdAndIsDeleteFalse(id);

        CycleResponse response = CycleResponse.builder()
                .id(cycle.getId())
                .name(cycle.getName())
                .startDate(cycle.getFromDate())
                .endDate(cycle.getEndDate())
                .build();

        return ResponseEntity.ok().body(
                ApiResponse.builder().code(commonProperties.getCODE_SUCCESS())
                        .message(commonProperties.getMESSAGE_SUCCESS())
                        .data(response).build()
        );
    }
}
