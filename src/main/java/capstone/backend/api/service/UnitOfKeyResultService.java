package capstone.backend.api.service;

import capstone.backend.api.dto.MeasureDto;
import capstone.backend.api.entity.UnitOfKeyResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UnitOfKeyResultService {
    UnitOfKeyResult getUnitById(long id);

    ResponseEntity<?> getAllMeasure(int page, int size, String sort, String jwtToken) throws Exception;

    ResponseEntity<?> createMeasure(MeasureDto unit, String jwtToken) throws Exception;

    ResponseEntity<?> updateMeasure(long id, MeasureDto unit, String jwtToken) throws Exception;

    ResponseEntity<?> deleteMeasure(long id, String jwtToken) throws Exception;

    ResponseEntity<?> searchMeasure(String name, int page, int size, String sort, String jwtToken);

    ResponseEntity<?> getListMetaDataMeasureUnit() throws Exception;

    ResponseEntity<?> getMeasureById(long id, String jwtToken);
}
