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
    private int startValue;
    private long measureUnitId;
    private int targetValue;
    private String reference;
    private int valueObtained;
}
