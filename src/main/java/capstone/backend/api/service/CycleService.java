package capstone.backend.api.service;

import capstone.backend.api.entity.Cycle;
import org.springframework.stereotype.Service;

@Service
public interface CycleService {

    Cycle getCycleById(long id) throws Exception;

}
