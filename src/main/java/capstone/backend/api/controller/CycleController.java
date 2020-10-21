package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.impl.CycleServiceImpl;
import capstone.backend.api.service.impl.ObjectiveServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/cycle")
public class CycleController {
    private CommonProperties commonProperties;

    private static final Logger logger = LoggerFactory.getLogger(CycleController.class);

    CycleServiceImpl cycleService;

    @GetMapping(path = "/current/{id}")
    public ResponseEntity<?> getCurrentCycle(@PathVariable(value = "id") long id){
        try {
            return cycleService.getCurrentCycle(id);
        } catch (Exception e) {
            logger.error("get current cycle failed");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }
}
