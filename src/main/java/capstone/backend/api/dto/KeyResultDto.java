package capstone.backend.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private double targetedValue;
    private String reference;
    private double valueObtained;
}
