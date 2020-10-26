package capstone.backend.api.entity.ApiResponse;

import capstone.backend.api.entity.ApiResponse.Objective.ObjectiveResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectObjectiveResponse {
    private long id;
    private String name;
    private String position;
    private List<ObjectiveResponse> objectives;
}

