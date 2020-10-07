package capstone.backend.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class KeyResultDto {

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
