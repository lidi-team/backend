package capstone.backend.api.service.impl;

import capstone.backend.api.entity.Execute;
import capstone.backend.api.repository.ExecuteRepository;
import capstone.backend.api.service.ExecuteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
@AllArgsConstructor
public class ExecuteServiceImpl implements ExecuteService {
    ExecuteRepository executeRepository;

    @Override
    public ArrayList<Execute> getListExecuteByUserId(long id) {

        return executeRepository.findAllByUserId(id)
                .map(Arrays::asList).map(ArrayList::new)
                .orElseGet(ArrayList::new);
    }

    @Override
    public Execute getExecuteByUserIdAndProjectId(long userId, long projectId) throws Exception {
        return executeRepository.findByUserIdAndProjectId(userId, projectId).orElse(null);
    }
}
