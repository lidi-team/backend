package capstone.backend.api.entity.ApiResponse.Objective;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveTitleResponse {
    private long id;
    private String title;
}
