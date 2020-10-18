package capstone.backend.api.service.impl;

import capstone.backend.api.entity.UnitOfKeyResult;
import capstone.backend.api.repository.UnitOfKeyResultRepository;
import capstone.backend.api.service.UnitOfKeyResultService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UnitOfKeyResultServiceImpl implements UnitOfKeyResultService {

    UnitOfKeyResultRepository unitRepository;

    @Override
    public UnitOfKeyResult getUnitById(long id){
        return unitRepository.findById(id).orElse(null);
    }
}
