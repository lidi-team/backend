package capstone.backend.api.entity.ApiResponse;

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

    @NotNull
    private String content;

    @NonNull
    private long measureUnitId;

    private String reference;

}
