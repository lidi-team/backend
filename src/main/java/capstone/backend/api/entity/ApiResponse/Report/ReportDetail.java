package capstone.backend.api.entity.ApiResponse.Report;

import capstone.backend.api.entity.ApiResponse.KeyResult.KeyResultCheckin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetail {
    private long id;
    private double valueObtained;
    private double confidentLevel;
    private String progress;
    private String problems;
    private String plans;
    private KeyResultCheckin keyResult;
}
