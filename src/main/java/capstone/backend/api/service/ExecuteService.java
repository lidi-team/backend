package capstone.backend.api.service;

import capstone.backend.api.entity.Execute;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public interface ExecuteService {
    ArrayList<Execute> getListExecuteByUserId(long id);
}
