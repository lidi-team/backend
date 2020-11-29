package capstone.backend.api.entity.ApiResponse.Objective;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveInferior {
    private long id;
    private double progress;
    private String title;
    private Date createAt;
}
