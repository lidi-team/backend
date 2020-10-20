package capstone.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeyResultDto {

    private long id;
    private String content;
    private long parentId;
    private double startValue;
    private long measureUnitId;
    private double targetValue;
    private String reference;
    private double valueObtained;
}
