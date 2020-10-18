package capstone.backend.api.service.impl;

import capstone.backend.api.entity.Cycle;
import capstone.backend.api.repository.CycleRepository;
import capstone.backend.api.service.CycleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CycleServiceImpl implements CycleService {

    private CycleRepository cycleRepository;

    @Override
    public Cycle getCycleById(long id) throws Exception {
        return cycleRepository.findById(id).orElse(null);
    }
}
