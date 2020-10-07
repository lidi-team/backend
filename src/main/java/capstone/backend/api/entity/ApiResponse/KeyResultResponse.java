package capstone.backend.api.entity.ApiResponse;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyResultResponse {
    private long id;

    private int startValue;

    private int valueObtained;

    private int targetedValue;

    @NotNull
    private String content;

    @NonNull
    private long measureUnitId;

    private String common;

}
