package capstone.backend.api.service;

import capstone.backend.api.entity.UnitOfKeyResult;
import org.springframework.stereotype.Service;

@Service
public interface UnitOfKeyResultService {
    UnitOfKeyResult getUnitById(long id);
}
