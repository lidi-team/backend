package capstone.backend.api.entity.ApiResponse.Objective;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlignObjectiveResponse {
    private long id;
    private String name;
    private int type;
    private String user;
}
