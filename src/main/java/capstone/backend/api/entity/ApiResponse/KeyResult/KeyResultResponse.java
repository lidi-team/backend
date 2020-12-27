package capstone.backend.api.entity.ApiResponse.KeyResult;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyResultResponse {
    private long id;

    private double startValue;

    private double valueObtained;

    private double targetedValue;

    private long measureUnitId;

    private double progress;

    @NotNull
    private String content;

    private String measureUnitName;

    private String reference;

}
