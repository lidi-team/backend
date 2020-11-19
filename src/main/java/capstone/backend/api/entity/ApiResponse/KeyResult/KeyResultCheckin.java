package capstone.backend.api.entity.ApiResponse.KeyResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyResultCheckin {
    private long id;
    private double valueObtained;
    private double targetedValue;
    private String content;
}
