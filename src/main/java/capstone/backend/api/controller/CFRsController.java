package capstone.backend.api.controller;

import capstone.backend.api.configuration.CommonProperties;
import capstone.backend.api.dto.CreateCfrDto;
import capstone.backend.api.entity.ApiResponse.ApiResponse;
import capstone.backend.api.service.impl.CfrServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/cfrs")
public class CFRsController {
    private static final Logger logger = LoggerFactory.getLogger(CFRsController.class);

    private final CommonProperties commonProperties;

    private final CfrServiceImpl cfrService;

    @ApiOperation(value = "Danh sách waiting feedback")
    @GetMapping(path = "/list-waiting")
    public ResponseEntity<?> getListWaiting(
            @ApiParam(value = "page", required = true)
            @RequestParam(name = "page") int page,
            @ApiParam(value = "limit")
            @RequestParam(name = "limit") int limit,
            @ApiParam(value = "token",required = true)
            @RequestHeader(name = "Authorization") String token) {
        try {
            return cfrService.getListWaiting(page, limit, token);
        } catch (Exception e) {
            logger.error("get list waiting cfrs");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Danh sách cfrs")
    @GetMapping(path = "/history")
    public ResponseEntity<?> getHistoryCfr(
            @ApiParam(value = "page", required = true)
            @RequestParam(name = "page") int page,
            @ApiParam(value = "limit", required = true)
            @RequestParam(name = "limit") int limit,
            @ApiParam(value = "cycleId",required = true)
            @RequestParam(name = "cycleId") long cycleId,
            @ApiParam(value = "type",required = true)
            @RequestParam(name = "type") int type,
            @ApiParam(value = "token",required = true)
            @RequestHeader(name = "Authorization") String token
            ) {
        try {
            return cfrService.getHistoryCfrs(page,limit,cycleId,type,token);
        } catch (Exception e) {
            logger.error("get list history cfrs");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Danh sách ")
    @GetMapping(path = "/user-star")
    public ResponseEntity<?> getUserStar(
            @ApiParam(value = "cycleId")
            @RequestParam(name = "cycleId") long cycleId
    ) {
        try {
            return cfrService.getUserStar(cycleId);
        } catch (Exception e) {
            logger.error("get list history cfrs");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "Detail cfr")
    @GetMapping(path = "/detail/{id}")
    public ResponseEntity<?> getDetailCfr(
            @ApiParam(value = "id")
            @PathVariable(name = "id") long id
    ) {
        try {
            return cfrService.getDetailCfr(id);
        } catch (Exception e) {
            logger.error("get list history cfrs");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

    @ApiOperation(value = "tao moi cfr")
    @PostMapping(path = "")
    public ResponseEntity<?> createCfr(
            @RequestBody() CreateCfrDto dto,
            @RequestHeader(value = "Authorization") String token
    ) {
        try {
            return cfrService.createCfr(dto, token);
        } catch (Exception e) {
            logger.error("create");
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .code(commonProperties.getCODE_UNDEFINE_ERROR())
                            .message(commonProperties.getMESSAGE_UNDEFINE_ERROR()).build()
            );
        }
    }

}
