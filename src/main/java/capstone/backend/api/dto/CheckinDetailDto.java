package capstone.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckinDetailDto {
    private long id;
    private double targetValue;
    private double valueObtained;
    private double confidentLevel;
    private String progress;
    private String problems;
    private String plans;
    private long keyResultId;
}
