package capstone.backend.api.entity.ApiResponse.Objective;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectOfUserResponse {
    private long id;
    private String name;
    private String position;
    private boolean isPm;
    private boolean remove;
    private List<ObjectiveProjectItem> objectives;
}
