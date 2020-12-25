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

    private double progress;

    @NotNull
    private String content;

    @NonNull
    private String measureUnitName;

    private String reference;

}
